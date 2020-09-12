package com.onion.ktatractcollection.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class TractPicture(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var tractId: UUID,
    val isFromDevice: Boolean,
    val photoFilename: String = "IMG_$id.jpg"
) {}