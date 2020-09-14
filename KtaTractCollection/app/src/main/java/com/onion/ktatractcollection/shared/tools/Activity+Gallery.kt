package com.onion.ktatractcollection.shared.tools

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore

fun buildGalleryIntent(): Intent {
    val getContentIntent = Intent(Intent.ACTION_GET_CONTENT)
    getContentIntent.type = "image/*"

    return getContentIntent
}