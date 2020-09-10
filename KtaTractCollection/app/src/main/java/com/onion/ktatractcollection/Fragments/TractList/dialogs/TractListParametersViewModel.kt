package com.onion.ktatractcollection.Fragments.TractList.dialogs

import androidx.lifecycle.ViewModel
import com.onion.ktatractcollection.Fragments.TractList.TractListParameters
import com.onion.ktatractcollection.Models.Tract

class TractListParametersViewModel: ViewModel() {

    /**
     * Methods
     */
    var parameters = TractListParameters()

    var sortBy: TractListParameters.SortBy
        get() = parameters.sortOption
        set(value) { parameters.sortOption = value }

    var sortOrder: TractListParameters.SortOrder
        get() = parameters.sortOrder
        set(value) { parameters.sortOrder = value }

    /**
     * Properties
     */
    fun getDisplayedTracts(tracts: List<Tract>): List<Tract> {
        return parameters.sortList(tracts)
    }
}