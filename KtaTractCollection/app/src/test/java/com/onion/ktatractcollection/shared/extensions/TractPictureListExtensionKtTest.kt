package com.onion.ktatractcollection.shared.extensions

import com.google.common.truth.Truth.assertThat
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.Models.TractPicture
import junit.framework.TestCase
import org.junit.Test

class TractPictureListExtensionKtTest : TestCase() {

    @Test
    fun testGroupedByTractId() {
        val tract1 = Tract()
        val tract2 = Tract()
        val tract3 = Tract()

        val pictures = listOf(
            TractPicture(tractId = tract1.id),
            TractPicture(tractId = tract2.id),
            TractPicture(tractId = tract3.id),
            TractPicture(tractId = tract1.id),
            TractPicture(tractId = tract2.id),
            TractPicture(tractId = tract3.id)
        )

        // When
        val result = pictures.groupedByTractId()

        // Then
        assertThat(result.containsKey(tract1.id)).isTrue()
        assertThat(result.containsKey(tract2.id)).isTrue()
        assertThat(result.containsKey(tract3.id)).isTrue()

        assertEquals((result[tract1.id] ?: error("")).size, 2)
        assertEquals((result[tract2.id] ?: error("")).size, 2)
        assertEquals((result[tract3.id] ?: error("")).size, 2)
    }

    @Test
    fun testGroupedByTractId_OneTract() {
        val tract1 = Tract()

        val pictures = listOf(
            TractPicture(tractId = tract1.id),
            TractPicture(tractId = tract1.id),
            TractPicture(tractId = tract1.id),
            TractPicture(tractId = tract1.id)
        )

        // When
        val result = pictures.groupedByTractId()

        // Then
        assertThat(result.containsKey(tract1.id)).isTrue()
        assertThat(result.keys.size).isEqualTo(1)
        assertEquals((result[tract1.id] ?: error("")).size, pictures.size)
    }

    @Test
    fun testGroupedByTractId_NoPictures() {
        val pictures = listOf<TractPicture>()

        // When
        val result = pictures.groupedByTractId()

        // Then
        assertThat(result.keys.size).isEqualTo(0)
    }
}