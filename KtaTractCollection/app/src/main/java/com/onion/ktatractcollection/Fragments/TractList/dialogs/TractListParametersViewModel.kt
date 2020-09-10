package com.onion.ktatractcollection.Fragments.TractList.dialogs

import androidx.lifecycle.ViewModel
import com.onion.ktatractcollection.Fragments.TractList.TractListParameters
import com.onion.ktatractcollection.Models.Tract

class TractListParametersViewModel: ViewModel() {
    var parameters = TractListParameters()

    var sortOption: TractListParameters.Sort
        get() = parameters.sortOption
        set(value) { parameters.sortOption = value }


    fun getDisplayedTracts(tracts: List<Tract>): List<Tract> {
        return sortOption.sortTractListMethod(tracts)
    }
}