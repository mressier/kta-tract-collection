package com.unicorpdev.ktatract.fragments.TractList

import android.content.Context
import android.net.Uri
import android.util.Log
import com.unicorpdev.ktatract.models.Tract
import com.unicorpdev.ktatract.models.TractPicture
import com.unicorpdev.ktatract.shared.tools.collection.CollectionExporter
import com.unicorpdev.ktatract.shared.tools.collection.CollectionImporter
import com.unicorpdev.ktatract.shared.viewmodel.RepositoryViewModel
import java.io.FileNotFoundException
import java.util.*

class AllTractsViewModel: RepositoryViewModel() {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    /** Saved repository data **/

    var savedTracts: List<Tract> = listOf()
    var savedPictures: List<TractPicture> = listOf()

    /***********************************************************************************************
     * Methods
     **********************************************************************************************/

    fun deleteTract(tractId: UUID) {
        val tract = savedTracts.find { it.id == tractId }

        tract?.let { tractRepository.deleteTract(it) }
    }

    /**
     * Pictures
     */

    fun updateTractIsFavorite(tractId: UUID, isFavorite: Boolean) {
        val tract = savedTracts.find { it.id == tractId  }

        tract?.let {
            it.isFavorite = isFavorite
            tractRepository.updateTract(it)
        }
    }

    /**
     * Export
     */

    fun exportCollection(context: Context, destination: Uri) {
        CollectionExporter(context).export(destination, savedTracts, savedPictures)
    }

    /**
     * Import
     */

    @Throws(FileNotFoundException::class)
    fun importCollection(context: Context, source: Uri) {
        val importer = CollectionImporter(context)

        try {
            importer.unzipFile(source)
        } catch (e: Error) {
            Log.e("pouet", "Error $e")
            throw e
        }

        val tracts = importer.importTracts() ?: listOf()
        tractRepository.addTracts(tracts)

        val pictures = importer.importPictures() ?: listOf()
        tractRepository.addPictures(pictures)
    }
}