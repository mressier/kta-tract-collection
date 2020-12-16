package com.unicorpdev.ktatract.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class TractCollection(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var title: String = "",
    var description: String = "",
    var imageFilename: String? = null
) {
    override fun equals(other: Any?): Boolean {
        val collection = other as? TractCollection ?: return super.equals(other)
        return collection.id == id
                && collection.title == title
                && collection.description == description
                && collection.imageFilename == imageFilename
    }
}