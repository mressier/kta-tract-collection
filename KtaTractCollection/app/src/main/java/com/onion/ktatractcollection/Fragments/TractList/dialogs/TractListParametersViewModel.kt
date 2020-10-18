package com.onion.ktatractcollection.Fragments.TractList.dialogs

import androidx.lifecycle.ViewModel
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

    var displayMode: TractListParameters.DisplayMode
        get() = parameters.displayMode
        set(value) { parameters.displayMode = value }

    /**
     * Properties
     */
    fun getDisplayedTracts(tracts: List<Tract>): List<Tract> {
        return parameters.sortList(tracts)
    }

    fun reverseDisplayMode(): TractListParameters.DisplayMode {
        parameters.reverseDisplayMode()
        return parameters.displayMode
    }
}