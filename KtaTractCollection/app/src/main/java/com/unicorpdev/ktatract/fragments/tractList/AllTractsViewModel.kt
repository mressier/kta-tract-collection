package com.unicorpdev.ktatract.fragments.tractList

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
     * Methods
     **********************************************************************************************/

    fun deleteTract(tractId: UUID) {
        val tract = tractRepository.getTract(tractId)
        tract?.let { tractRepository.deleteTract(it) }
    }

    /**
     * Pictures
     */

    fun updateTractIsFavorite(tractId: UUID, isFavorite: Boolean) {
        val tract = tractRepository.getTract(tractId)

        tract?.let {
            it.isFavorite = isFavorite
            tractRepository.updateTract(it)
        }
    }

    /**
     * Export
     */

    fun exportCollection(context: Context, destination: Uri) {
        val collections = tractRepository.getCollections()
        val tracts = tractRepository.getTracts()
        val pictures = tractRepository.getPictures()
        CollectionExporter(context).export(destination, collections, tracts, pictures)
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

        val collections = importer.importCollections() ?: listOf()
        tractRepository.addCollections(collections)

        val tracts = importer.importTracts() ?: listOf()
        tractRepository.addTracts(tracts)

        val pictures = importer.importPictures() ?: listOf()
        tractRepository.addPictures(pictures)
    }
}