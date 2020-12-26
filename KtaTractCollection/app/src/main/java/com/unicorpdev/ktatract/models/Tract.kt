package com.unicorpdev.ktatract.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.unicorpdev.ktatract.shared.extensions.shortString
import java.util.*

/**
 * A class representing a [Tract]: this is a piece of paper or an object found in the catacombs,
 * usually concealed by a cataphile between two stones or somewhere else.
 */
@Entity
data class Tract(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    /* The collection containing the tract */
    var collectionId: UUID,
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

    override fun equals(other: Any?): Boolean {
        val tract = other as? Tract ?: return super.equals(other)

        return tract.id == id
                && tract.collectionId == tract.collectionId
                && tract.databaseAddingDate == databaseAddingDate
                && tract.author == author
                && tract.discoveryDate == discoveryDate
                && tract.dating == dating
                && tract.comment == comment
                && tract.isFavorite == isFavorite
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + collectionId.hashCode()
        result = 31 * result + databaseAddingDate.hashCode()
        result = 31 * result + author.hashCode()
        result = 31 * result + discoveryDate.hashCode()
        result = 31 * result + (dating?.hashCode() ?: 0)
        result = 31 * result + comment.hashCode()
        result = 31 * result + isFavorite.hashCode()
        return result
    }
}
