package com.onion.ktatractcollection.shared.tools

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore

fun Activity.buildGalleryIntent(): Intent {
    val getContentIntent = Intent(Intent.ACTION_GET_CONTENT)
    getContentIntent.type = "image/*"

    val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    pickIntent.type = "image/*"

    val chooserIntent = Intent.createChooser(getContentIntent, "Select Image")
    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

    return chooserIntent
}