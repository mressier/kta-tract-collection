package com.onion.ktatractcollection.Fragments.TractList

import android.content.Context
import android.net.Uri
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.onion.ktatractcollection.Database.TractRepository
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.Models.TractPicture
import com.onion.ktatractcollection.shared.extensions.filterByTractId
import com.onion.ktatractcollection.shared.tools.collection.CollectionExporter
import com.onion.ktatractcollection.shared.tools.collection.CollectionImporter
import com.onion.ktatractcollection.shared.viewmodel.RepositoryViewModel
import java.io.FileNotFoundException
import java.util.*


class TractWithPicture(val tract: Tract, var pictures: List<TractPicture> = listOf()) {}

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
        val pictures = savedPictures.filterByTractId(tractId)
        val tract = savedTracts.find { it.id == tractId }

        tractRepository.deletePictures(pictures)
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