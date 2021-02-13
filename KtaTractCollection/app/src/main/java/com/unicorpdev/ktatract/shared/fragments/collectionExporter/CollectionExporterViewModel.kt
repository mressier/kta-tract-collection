package com.unicorpdev.ktatract.shared.fragments.collectionExporter

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.ViewModel
import com.unicorpdev.ktatract.database.TractRepository
import com.unicorpdev.ktatract.models.Tract
import com.unicorpdev.ktatract.models.TractCollection
import com.unicorpdev.ktatract.models.TractPicture
import com.unicorpdev.ktatract.shared.tools.collection.CollectionExporter
import com.unicorpdev.ktatract.shared.tools.collection.CollectionFiles
import com.unicorpdev.ktatract.shared.tools.collection.CollectionImporter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.io.FileNotFoundException
import java.io.IOException
import java.lang.Exception
import java.util.*

class CollectionExporterViewModel: ViewModel() {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private val tractRepository: TractRepository by lazy {
        TractRepository.get()
    }

    var collectionId: UUID? = null

    /***********************************************************************************************
     * Export
     **********************************************************************************************/

    fun exportCollection(context: Context, destination: Uri) {
        val collections = collectionId?.let { id ->
            tractRepository.getCollection(id)?.let { listOf(it) } ?: listOf()
        } ?: tractRepository.getCollections()

        val tracts = collectionId?.let {
            tractRepository.getTractsForCollection(it)
        } ?: tractRepository.getTracts()

        val pictures = collectionId?.let {
            tractRepository.getPicturesForCollection(it)
        } ?: tractRepository.getPictures()

        CollectionExporter(context).export(destination, collections, tracts, pictures)
    }

    /***********************************************************************************************
     * Import
     **********************************************************************************************/

    enum class ImportMethod {
        REPLACE,
        IGNORE
    }

    interface ImportCallback {
        fun onSuccess()
        fun onFailed(error: Error)
    }

    @Throws(FileNotFoundException::class)
    fun importCollection(activity: Activity, source: Uri, callback: ImportCallback?) {
        GlobalScope.async {
            val importer = CollectionImporter(activity)

            try {
                importer.unzipFile(source, tractRepository.filesDir, CollectionFiles.REQUIRED_FILES_IN_ZIP)
                Log.d(TAG, "Unzip complete")
            } catch (e: FileNotFoundException) {
                activity.runOnUiThread {
                    callback?.onFailed(Error(e))
                }
            }

            Log.d(TAG, "Starting import")

            val collections = importer.getCollectionsFromJson() ?: listOf()
            val tracts = importer.getTractsFromJson() ?: listOf()
            val pictures = importer.getPicturesFromJson() ?: listOf()

            addCollections(collections)
            addTracts(tracts)
            addPictures(pictures)

            activity.runOnUiThread { callback?.onSuccess() }
        }
    }

    private fun addCollections(collections: List<TractCollection>) {
        tractRepository.addCollections(collections)
    }

    private fun addTracts(tracts: List<Tract>) {
        val oldTractsDuplicates = tractRepository.getTracts(tracts.map { it.id })
        val oldLikedTracts = oldTractsDuplicates.filter { it.isFavorite }

        val newLikedTracts = tracts.map { tract ->
            val liked = oldLikedTracts.firstOrNull { it.id == tract.id }?.isFavorite
            tract.apply { isFavorite = liked ?: false }
        }
        tractRepository.addTracts(newLikedTracts)
    }

    private fun addPictures(pictures: List<TractPicture>) {
        tractRepository.addPictures(pictures)
    }

    companion object {
        private val TAG = CollectionExporterViewModel::class.simpleName ?: "Default"
    }
}