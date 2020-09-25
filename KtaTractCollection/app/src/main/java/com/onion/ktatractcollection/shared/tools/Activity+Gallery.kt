package com.onion.ktatractcollection.shared.tools

import android.content.Intent

fun buildGalleryIntent(retainDocument: Boolean = false,
                       allowMultipleFiles: Boolean = false): Intent {
    val action =
        if (retainDocument) { Intent.ACTION_OPEN_DOCUMENT } else { Intent.ACTION_GET_CONTENT }
    val galleryIntent = Intent(action)

    galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultipleFiles)
    galleryIntent.type = "image/*"

    return galleryIntent
}