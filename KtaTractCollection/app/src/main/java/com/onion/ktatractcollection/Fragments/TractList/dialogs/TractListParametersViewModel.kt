package com.onion.ktatractcollection.Fragments.TractList.dialogs

import androidx.lifecycle.ViewModel
import com.onion.ktatractcollection.Models.Tract
import java.util.*

class TractListParametersViewModel: ViewModel() {

    /**
     * Properties
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

    var showOnlyFavorites: Boolean
        get() = parameters.showOnlyFavorites
        set(value) { parameters.showOnlyFavorites = value }

    var searchText: String? = null

    val isInSearchMode: Boolean
        get() = !searchText.isNullOrBlank() || showOnlyFavorites

    /**
     * Methods
     */

    fun getDisplayedTracts(tracts: List<Tract>): List<Tract> {
        val sortedTracts = sortTracts(tracts)
        return filterTractsWithSearchText(sortedTracts)
    }

    /**
     * Private Methods
     */

    private fun sortTracts(tracts: List<Tract>): List<Tract> {
        return parameters.filterAndSortList(tracts)
    }

    private fun filterTractsWithSearchText(tracts: List<Tract>): List<Tract> {
        val text = searchText?.toLowerCase(Locale.ROOT) ?: ""

        if (text.isBlank()) { return tracts }

        return tracts.filter { it.contains(text) }
    }
}