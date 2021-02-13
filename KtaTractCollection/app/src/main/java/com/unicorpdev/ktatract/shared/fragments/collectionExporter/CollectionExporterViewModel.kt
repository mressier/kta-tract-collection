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
import com.unicorpdev.ktatract.shared.tools.collection.CollectionExporter
import com.unicorpdev.ktatract.shared.tools.collection.CollectionImporter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.io.FileNotFoundException
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
    fun importCollection(activity: Activity, source: Uri, method: ImportMethod, callback: ImportCallback?) {
        GlobalScope.async {
            val importer = CollectionImporter(activity)

            try {
                importer.unzipFile(source)
            } catch (e: Error) {
                activity.runOnUiThread { callback?.onFailed(e) }
            }

            val collections = importer.getCollectionsFromJson() ?: listOf()
            val tracts = importer.getTractsFromJson() ?: listOf()
            val pictures = importer.getPicturesFromJson() ?: listOf()

            when (method) {
                ImportMethod.REPLACE -> {
                    tractRepository.addCollections(collections)
                    tractRepository.addTracts(tracts)
                    tractRepository.addPictures(pictures)
                }
                ImportMethod.IGNORE -> {
                    tractRepository.addCollectionsIfNotExist(collections)
                    tractRepository.addTractsIfNotExists(tracts)
                    tractRepository.addPicturesIfNotExist(pictures)
                }
            }

            activity.runOnUiThread { callback?.onSuccess() }
        }
    }

}