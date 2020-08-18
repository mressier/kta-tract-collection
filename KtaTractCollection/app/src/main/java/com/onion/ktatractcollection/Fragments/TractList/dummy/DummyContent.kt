package com.onion.ktatractcollection.Fragments.TractList.dummy

import com.onion.ktatractcollection.Models.Tract
import java.util.*

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    val ITEMS: MutableList<Tract> = ArrayList()

    /**
     * A map of sample (dummy) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, Tract> = HashMap()

    private val COUNT = 5

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(createDummyItem(i))
        }
    }

    private fun addItem(item: Tract) {
        ITEMS.add(item)
    }

    private fun createDummyItem(position: Int): Tract {
        return Tract(author = "Dummy", discoveryDate = Date(), comment = makeDetails(position))
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0..position - 1) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

}