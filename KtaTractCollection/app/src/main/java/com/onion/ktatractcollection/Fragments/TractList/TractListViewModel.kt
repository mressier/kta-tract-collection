package com.onion.ktatractcollection.Fragments.TractList

import androidx.lifecycle.ViewModel
import com.onion.ktatractcollection.Database.TractRepository
import com.onion.ktatractcollection.Models.Tract

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

}