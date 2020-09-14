package com.onion.ktatractcollection.Fragments.TractList

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.onion.ktatractcollection.Database.TractRepository
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.Models.TractPicture
import java.io.File
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

    var tractsWithPicture: List<TractWithPicture> = listOf()

    fun saveTract(tract: Tract) {
        tractRepository.addTract(tract)
    }

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
        val newTractsWithPicture = tracts.map { tract ->
            val oldPicture = getSavedTractWithPictures(tract.id)?.pictures
            TractWithPicture(tract, oldPicture ?: listOf())
        }

        tractsWithPicture = newTractsWithPicture
    }

    fun addPicturesToTractItem(tractId: UUID, pictures: List<TractPicture>) {
        tractsWithPicture.find { it.tract.id == tractId }?.pictures = pictures
    }

    fun getPictures(tractId: UUID): LiveData<List<TractPicture>> {
        return tractRepository.getPictures(tractId)
    }

    private fun getSavedTractWithPictures(tractId: UUID): TractWithPicture? {
        return tractsWithPicture.find { it.tract.id == tractId }
    }

}