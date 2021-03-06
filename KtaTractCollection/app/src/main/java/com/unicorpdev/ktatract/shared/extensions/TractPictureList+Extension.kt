package com.unicorpdev.ktatract.shared.extensions

import com.unicorpdev.ktatract.models.TractPicture
import java.util.*

fun List<TractPicture>.groupedByTractId(): Map<UUID, List<TractPicture>> {
    val result: MutableMap<UUID, MutableList<TractPicture>> = mutableMapOf()

    this.forEach { picture ->
        val id = picture.tractId

        if (!result.containsKey(id)) {
            result[id] = mutableListOf()
        }

        result[id]?.add(picture)
    }

    return result
}

fun List<TractPicture>.filterByTractId(tractId: UUID): List<TractPicture> {
    return this.filter { it.tractId == tractId }
}