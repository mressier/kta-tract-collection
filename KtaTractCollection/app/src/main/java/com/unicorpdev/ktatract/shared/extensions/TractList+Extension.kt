package com.unicorpdev.ktatract.shared.extensions

import com.unicorpdev.ktatract.fragments.TractList.parameters.TractListParameters
import com.unicorpdev.ktatract.models.Tract
import com.unicorpdev.ktatract.models.TractPicture
import com.unicorpdev.ktatract.models.TractWithPicture
import java.util.*

fun List<Tract>.mergeWithPictures(pictures: List<TractPicture>): List<TractWithPicture> {
    val picturesByTractId = pictures.groupedByTractId()

    return this.map { tract ->
        TractWithPicture(tract, picturesByTractId[tract.id] ?: listOf())
    }
}

fun List<Tract>.sortAndFilter(parameters: TractListParameters): List<Tract> {
    return this.sort(parameters).filter(parameters)
}

fun List<Tract>.sort(parameters: TractListParameters): List<Tract> {
    val isNaturalOrder = parameters.isNaturalOrder

    return when (parameters.sortOption) {
        TractListParameters.SortOption.DEFAULT -> sortedBy(isNaturalOrder) { it.databaseAddingDate }
        TractListParameters.SortOption.AUTHOR -> sortedBy(isNaturalOrder) { it.author.toLowerCase(
            Locale.ROOT) }
        TractListParameters.SortOption.DATING -> sortedBy(isNaturalOrder) { it.dating }
        TractListParameters.SortOption.DISCOVERY_DATE -> sortedBy(isNaturalOrder) { it.discoveryDate }
    }
}

fun List<Tract>.filter(parameters: TractListParameters): List<Tract> {
    val favFilter = if (parameters.showOnlyFavorites) this.filter { it.isFavorite } else this
    val text = parameters.searchText.toLowerCase(Locale.ROOT)
    return if (text.isBlank()) favFilter else filter { it.contains(text) }
}
