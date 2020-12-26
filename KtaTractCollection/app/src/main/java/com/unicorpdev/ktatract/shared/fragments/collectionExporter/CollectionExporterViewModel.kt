package com.unicorpdev.ktatract.shared.fragments.collectionExporter

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.unicorpdev.ktatract.database.TractRepository
import com.unicorpdev.ktatract.shared.tools.collection.CollectionExporter
import com.unicorpdev.ktatract.shared.tools.collection.CollectionImporter
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
        val collections = tractRepository.getCollections()
        val tracts = tractRepository.getTracts()
        val pictures = tractRepository.getPictures()
        CollectionExporter(context).export(destination, collections, tracts, pictures)
    }

    /***********************************************************************************************
     * Import
     **********************************************************************************************/

    @Throws(FileNotFoundException::class)
    fun importCollection(context: Context, source: Uri) {
        val importer = CollectionImporter(context)

        try {
            importer.unzipFile(source)
        } catch (e: Error) {
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