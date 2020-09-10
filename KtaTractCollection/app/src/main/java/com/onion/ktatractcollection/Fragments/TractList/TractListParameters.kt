package com.onion.ktatractcollection.Fragments.TractList

import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.R

class TractListParameters() {

    /**
     * Parameters
     */
    enum class Sort(val stringId: Int, val sortTractListMethod: ((List<Tract>) -> List<Tract>)) {
        DEFAULT(R.string.tract_sort_default, { it }),
        AUTHOR(R.string.tract_sort_author, { list -> list.sortedBy { it.author } }),
        DISCOVERY_DATE(
            R.string.tract_sort_discovery_date,
            { list -> list.sortedBy { it.discoveryDate }}
        )
    }

    /**
     * Properties
     */
    var sortOption = Sort.DEFAULT
}