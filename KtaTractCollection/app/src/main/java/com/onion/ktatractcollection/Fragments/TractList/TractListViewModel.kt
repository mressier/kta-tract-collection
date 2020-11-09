package com.onion.ktatractcollection.Fragments.TractList

import android.net.Uri
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.onion.ktatractcollection.Database.TractRepository
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.Models.TractPicture
import com.onion.ktatractcollection.shared.tools.DatabaseZipper
import java.util.*


class TractWithPicture(val tract: Tract, var pictures: List<TractPicture> = listOf()) {}

class TractListViewModel: ViewModel() {

    /**
     * Properties
     */
    private val tractRepository = TractRepository.get()
    private val exporter = DatabaseZipper.get()

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
            TractWithPicture(tract, oldPicture ?: listOf())
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

    fun exportCollection(destination: Uri) {
        exporter.export(destination, tractsList, picturesList)
    }

    /**
     * Import
     */

    fun importCollection(source: Uri) {
        exporter.unzipFile(source)

        val tracts = exporter.importTracts() ?: listOf()
        tractRepository.addTracts(tracts)

        val pictures = exporter.importPictures() ?: listOf()
        tractRepository.addPictures(pictures)
    }
}