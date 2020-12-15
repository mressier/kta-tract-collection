package com.unicorpdev.ktatract.shared.extensions

import com.google.common.truth.Truth
import com.unicorpdev.ktatract.models.Tract
import com.unicorpdev.ktatract.models.TractPicture
import junit.framework.TestCase

class TractListExtensionKtTest : TestCase() {

    fun testMergeWithPictures() {
        val tract1 = Tract()
        val tract2 = Tract()
        val tracts = listOf(tract1, tract2)
        val pictures = listOf(
            TractPicture(tractId = tract1.id), TractPicture(tractId = tract2.id),
            TractPicture(tractId = tract1.id), TractPicture(tractId = tract1.id)
        )

        // When
        val result = tracts.mergeWithPictures(pictures)

        // Then
        Truth.assertThat(result.size).isEqualTo(tracts.size)

        Truth.assertThat(result.find { it.tract == tract1 }).isNotNull()
        Truth.assertThat(result.find { it.tract == tract1 }?.pictures?.size).isEqualTo(3)

        Truth.assertThat(result.find { it.tract == tract2 }).isNotNull()
        Truth.assertThat(result.find { it.tract == tract2 }?.pictures?.size).isEqualTo(1)
    }
}