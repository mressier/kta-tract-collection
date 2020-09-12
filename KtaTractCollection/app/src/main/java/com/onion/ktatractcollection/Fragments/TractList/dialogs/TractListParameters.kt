package com.onion.ktatractcollection.Fragments.TractList.dialogs

import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.R
import java.nio.channels.spi.AbstractSelector

class TractListParameters() {

    /**
     * Parameters
     */
    enum class SortBy(val stringId: Int) {
        DEFAULT(R.string.tract_sort_default),
        AUTHOR(R.string.tract_sort_author),
        DATING(R.string.tract_sort_dating),
        DISCOVERY_DATE( R.string.tract_sort_discovery_date)
    }

    enum class SortOrder(val stringId: Int) {
        ASCENDING(R.string.sort_order_ascending),
        DESCENDING(R.string.sort_order_descending)
    }

    /**
     * Properties
     */
    var sortOption = SortBy.DEFAULT
    var sortOrder = SortOrder.DESCENDING

    private val isNaturalOrder: Boolean
        get() = sortOrder == SortOrder.ASCENDING

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