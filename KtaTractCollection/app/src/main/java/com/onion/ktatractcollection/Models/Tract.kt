package com.onion.ktatractcollection.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.onion.ktatractcollection.shared.extensions.*
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
    var isFavorite: Boolean = false) {

    /**
     * Methods
     */

    fun contains(text: String): Boolean {
        return author.toLowerCase(Locale.ROOT).contains(text)
                || comment.toLowerCase(Locale.ROOT).contains(text)
                || (dating?.shortString ?: "").toLowerCase(Locale.ROOT).contains(text)
                || discoveryDate.shortString.toLowerCase(Locale.ROOT).contains(text)
    }
}
