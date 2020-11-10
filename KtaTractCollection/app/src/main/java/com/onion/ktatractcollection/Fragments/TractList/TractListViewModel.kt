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
import com.onion.ktatractcollection.shared.tools.collection.CollectionExporter
import com.onion.ktatractcollection.shared.tools.collection.CollectionImporter
import java.io.FileNotFoundException
import java.util.*


class TractWithPicture(val tract: Tract, var pictures: List<TractPicture> = listOf()) {}

class TractListViewModel: ViewModel() {

    /**
     * Properties
     */

    private val tractRepository = TractRepository.get()

    /**
     * Live Data
     */

    /** Tract list saved locally **/
    val tracts: LiveData<List<Tract>> = tractRepository.getTracts()

    private var tractsList: List<Tract> = listOf()
    private var picturesList: MutableList<TractPicture> = mutableListOf()

    var tractsWithPicture: List<TractWithPicture> = listOf()

    var recyclerViewState: Parcelable? = null

    /**
     * Methods
     */

    fun deleteTract(tract: Tract) {
        getSavedTractWithPictures(tract.id)?.pictures?.let {
            tractRepository.deletePictures(it)
        }
        tractRepository.deleteTract(tract)

        tractsWithPicture = tractsWithPicture.filter { it.tract.id != tract.id }
    }

    /**
     * Pictures
     */

    fun saveAsTractsWithPicture(tracts: List<Tract>) {
        tractsList = tracts

        val newTractsWithPicture = tracts.map { tract ->
            val oldPicture = getSavedTractWithPictures(tract.id)?.pictures
            val pictures = oldPicture ?: picturesList.filter { it.tractId == tract.id }
            TractWithPicture(tract, pictures)
        }

        tractsWithPicture = newTractsWithPicture
    }

    fun addPicturesToTractItem(tractId: UUID, pictures: List<TractPicture>) {
        val oldPictures = picturesList.filter { it.tractId == tractId }
        picturesList.removeAll(oldPictures)
        picturesList.addAll(pictures)

        getSavedTractWithPictures(tractId)?.pictures = pictures
    }

    fun getPictures(tractId: UUID): LiveData<List<TractPicture>> {
        return tractRepository.getPictures(tractId)
    }

    fun toggleLike(tractId: UUID) {
        getSavedTractWithPictures(tractId)?.tract?.let { tract ->
            tract.isFavorite = !tract.isFavorite
            tractRepository.updateTract(tract)
        }
    }

    fun getSavedTractWithPictures(tractId: UUID): TractWithPicture? {
        return tractsWithPicture.find { it.tract.id == tractId }
    }

    /**
     * Export
     */

    fun exportCollection(context: Context, destination: Uri) {
        CollectionExporter(context).export(destination, tractsList, picturesList)
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