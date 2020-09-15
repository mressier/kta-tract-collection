package com.onion.ktatractcollection.shared.tools

import android.content.Intent

fun buildGalleryIntent(retainDocument: Boolean = false): Intent {
    val action =
        if (retainDocument) { Intent.ACTION_OPEN_DOCUMENT } else { Intent.ACTION_GET_CONTENT }
    val getContentIntent = Intent(action)
    getContentIntent.type = "image/*"

    return getContentIntent
}