package com.onion.ktatractcollection.Fragments.TractList

import androidx.lifecycle.ViewModel
import com.onion.ktatractcollection.Database.TractRepository
import com.onion.ktatractcollection.Models.Tract
import java.io.File

class TractListItem(val tract: Tract, val photoFile: File) {}

class TractListViewModel: ViewModel() {

    /**
     * Properties
     */
    private val tractRepository = TractRepository.get()

    /**
     * Live Data
     */

    /** Tract list saved locally **/
    var tracts = tractRepository.getTracts()

    fun saveTract(tract: Tract) {
        tractRepository.addTract(tract)
    }

    fun deleteTract(tract: Tract) {
        tractRepository.deleteTract(tract)
    }

    fun getPhotoFile(tract: Tract): File {
        return tractRepository.getPhotoFile(tract)
    }

    fun getAsTractListItems(tracts: List<Tract>): List<TractListItem> {
        return tracts.map {
            TractListItem(it, getPhotoFile(it))
        }
    }

}