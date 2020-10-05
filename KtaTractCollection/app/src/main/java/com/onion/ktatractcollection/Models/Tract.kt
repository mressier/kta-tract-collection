package com.onion.ktatractcollection.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * A class representing a [Tract]: this is a piece of paper or an object found in the catacombs,
 * usually concealed by a cataphile between two stones or somewhere else.
 */
@Entity
data class Tract(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    /* Date of the creation of the tract in the database */
    val databaseAddingDate: Date = Date(),
    /* Author of the tract */
    var author: String = "",
    /* Date when tract has been discovered */
    var discoveryDate: Date = Date(),
    /* Date figuring on the tract */
    var dating: Date? = null,
    /* Comments about the tract */
    var comment: String = "",
    /* User set tract as a favorite */
    var isFavorite: Boolean = false) {}