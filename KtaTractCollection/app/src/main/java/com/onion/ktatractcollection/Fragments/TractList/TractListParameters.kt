package com.onion.ktatractcollection.Fragments.TractList

import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.R

class TractListParameters() {

    /**
     * Parameters
     */
    enum class SortBy(val stringId: Int, val sortTractElement: (Tract) -> Comparable<*>) {
        DEFAULT(R.string.tract_sort_default, { it.id }),
        AUTHOR(R.string.tract_sort_author, { it.author }),
        DISCOVERY_DATE(
            R.string.tract_sort_discovery_date,
            { it.discoveryDate }
        )
    }

    enum class SortOrder(val stringId: Int, val sortTractListMethod: (List<Tract>) -> List<Tract>) {
        ASCENDING(R.string.sort_order_ascending, { it }),
        DESCENDING(R.string.sort_order_descending, { it.reversed() })
    }

    /**
     * Properties
     */
    var sortOption = SortBy.DEFAULT
    var sortOrder = SortOrder.ASCENDING

    private val isNaturalOrder: Boolean
        get() = sortOrder == SortOrder.ASCENDING

    /**
     * Methods
     */
    fun sortList(tract: List<Tract>): List<Tract> {
        return when (sortOption) {
            SortBy.DEFAULT ->
                if (isNaturalOrder) { tract } else { tract.reversed() }
            SortBy.AUTHOR ->
                if (isNaturalOrder) { tract.sortedBy { it.author } } else { tract.sortedByDescending { it.author } }
            SortBy.DISCOVERY_DATE ->
                if (isNaturalOrder) { tract.sortedBy { it.discoveryDate } } else { tract.sortedByDescending { it.discoveryDate } }
        }
    }

}