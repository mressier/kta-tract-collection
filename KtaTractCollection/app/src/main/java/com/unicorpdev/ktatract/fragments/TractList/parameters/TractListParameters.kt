package com.unicorpdev.ktatract.fragments.TractList.parameters

import androidx.annotation.StringRes
import com.unicorpdev.ktatract.R
import java.io.Serializable

class TractListParameters: Serializable {

    /***********************************************************************************************
     * Inner Types
     **********************************************************************************************/

    enum class SortOption(@StringRes val stringId: Int) {
        DEFAULT(R.string.tract_sort_default),
        AUTHOR(R.string.tract_sort_author),
        DATING(R.string.tract_sort_dating),
        DISCOVERY_DATE( R.string.tract_sort_discovery_date)
    }

    enum class SortOrder(@StringRes val stringId: Int) {
        ASCENDING(R.string.sort_order_ascending),
        DESCENDING(R.string.sort_order_descending)
    }

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    var sortOption = SortOption.DEFAULT
    var sortOrder = SortOrder.DESCENDING
    var showOnlyFavorites = false
    var searchText = ""

    /** Computed **/

    val isInSearchMode: Boolean
        get() = searchText.isNotBlank() || showOnlyFavorites

    val isNaturalOrder: Boolean
        get() = sortOrder == SortOrder.ASCENDING

}
