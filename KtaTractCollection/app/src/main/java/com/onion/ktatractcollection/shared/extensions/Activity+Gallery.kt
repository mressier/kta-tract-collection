package com.onion.ktatractcollection.shared.extensions

import android.content.Intent

fun buildGalleryIntent(retainDocument: Boolean = false,
                       allowMultipleFiles: Boolean = false): Intent {
    val action =
        if (retainDocument) { Intent.ACTION_OPEN_DOCUMENT } else { Intent.ACTION_GET_CONTENT }
    val galleryIntent = Intent(action)

    if (retainDocument) {
        galleryIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        galleryIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
    }

    galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultipleFiles)
    galleryIntent.type = "image/*"

    return galleryIntent
}