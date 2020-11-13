package com.unicorpdev.ktatract.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class TractPicture(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var tractId: UUID,
    val photoFilename: String = randomFilename()
) {
    companion object {
        fun randomFilename(): String { return "IMG_${UUID.randomUUID()}.jpg" }
    }
}