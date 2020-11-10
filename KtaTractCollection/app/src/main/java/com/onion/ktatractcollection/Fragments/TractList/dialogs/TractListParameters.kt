package com.onion.ktatractcollection.Fragments.TractList.dialogs

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.R

class TractListParameters() {

    /**
     * Parameters
     */
    enum class SortBy(@StringRes val stringId: Int) {
        DEFAULT(R.string.tract_sort_default),
        AUTHOR(R.string.tract_sort_author),
        DATING(R.string.tract_sort_dating),
        DISCOVERY_DATE( R.string.tract_sort_discovery_date)
    }

    enum class SortOrder(@StringRes val stringId: Int) {
        ASCENDING(R.string.sort_order_ascending),
        DESCENDING(R.string.sort_order_descending)
    }

    enum class DisplayMode(val spanCount: Int, @DrawableRes val iconId: Int, @StringRes val titleId: Int) {
        LIST(1, R.drawable.ic_baseline_list_bulleted_24, R.string.show_as_list),
        GRID(2, R.drawable.ic_baseline_apps_24, R.string.show_as_grid);

        val reversed: DisplayMode
            get() = if (this == GRID) { LIST } else { GRID }
    }

    /**
     * Properties
     */
    var sortOption = SortBy.DEFAULT
    var sortOrder = SortOrder.DESCENDING
    var displayMode = DisplayMode.LIST
    var showOnlyFavorites = false

    val isList: Boolean
        get() = displayMode == DisplayMode.LIST

    private val isNaturalOrder: Boolean
        get() = sortOrder == SortOrder.ASCENDING

    /**
     * Methods
     */

    fun filterAndSortList(tract: List<Tract>): List<Tract> {
        val list = when (sortOption) {
            SortBy.DEFAULT -> tract.sortedBy(isNaturalOrder) { it.databaseAddingDate }
            SortBy.AUTHOR -> tract.sortedBy(isNaturalOrder) { it.author }
            SortBy.DATING -> tract.sortedBy(isNaturalOrder) { it.dating }
            SortBy.DISCOVERY_DATE -> tract.sortedBy(isNaturalOrder) { it.discoveryDate }
        }

        return if (showOnlyFavorites) list.filter { it.isFavorite } else list
    }

    fun reverseDisplayMode() {
        displayMode = displayMode.reversed
    }
}

/**
 * SortedBy Tools
 */
inline fun <T, R : Comparable<R>> Iterable<T>.sortedBy(
    ascending: Boolean,
    crossinline selector: (T) -> R?
): List<T> {
    return if (ascending) { sortedBy(selector) } else { sortedByDescending(selector) }
}