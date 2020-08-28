package com.onion.ktatractcollection.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Tract(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var author: String = "",
    var discoveryDate: Date = Date(),
    var comment: String = "") {

    val photoFilename
        get() = "IMG_$id.jpg"
}