package com.onion.ktatractcollection.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class TractPicture(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var tractId: UUID,
    val photoFilename: String = randomFilename()
) {
    val isFromCamera: Boolean
        get() = !isFromDevice

    companion object {
        fun randomFilename(): String { return "IMG_${UUID.randomUUID()}.jpg" }
    }
}