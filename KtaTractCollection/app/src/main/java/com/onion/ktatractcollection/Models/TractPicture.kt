package com.onion.ktatractcollection.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class TractPicture(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var tractId: UUID,
    val isFromDevice: Boolean,
    val photoFilename: String = randomFilename
) {
    val isFromCamera: Boolean
        get() = !isFromDevice

    companion object {
        var randomFilename: String = "IMG_${UUID.randomUUID()}.jpg"
    }
}