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
        LIST(1, R.drawable.ic_baseline_list_bulleted_24, R.string.list_tract),
        GRID(3, R.drawable.ic_baseline_grid_24, R.string.grid_tract)
    }

    /**
     * Properties
     */
    var sortOption = SortBy.DEFAULT
    var sortOrder = SortOrder.DESCENDING
    var displayMode = DisplayMode.LIST

    private val isNaturalOrder: Boolean
        get() = sortOrder == SortOrder.ASCENDING

    val reversedDisplayMode: DisplayMode
        get() {
            return if (displayMode == DisplayMode.GRID) {
                DisplayMode.LIST
            } else {
                DisplayMode.GRID
            }
        }

    /**
     * Methods
     */

    fun sortList(tract: List<Tract>): List<Tract> {
        return when (sortOption) {
            SortBy.DEFAULT -> tract.sortedBy(isNaturalOrder) { it.databaseAddingDate }
            SortBy.AUTHOR -> tract.sortedBy(isNaturalOrder) { it.author }
            SortBy.DATING -> tract.sortedBy(isNaturalOrder) { it.dating }
            SortBy.DISCOVERY_DATE -> tract.sortedBy(isNaturalOrder) { it.discoveryDate }
        }
    }

    fun reverseDisplayMode() {
        displayMode = reversedDisplayMode
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