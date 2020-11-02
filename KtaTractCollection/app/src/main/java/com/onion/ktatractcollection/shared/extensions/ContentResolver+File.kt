package com.onion.ktatractcollection.shared.extensions

import android.content.ContentResolver
import android.net.Uri
import android.os.FileUtils

fun ContentResolver.copy(origin: Uri, destination: Uri): Boolean {
    val input = openInputStream(origin)
    val output = openOutputStream(destination)

    return if (input == null || output == null) { false } else {
        FileUtils.copy(input, output)
        true
    }
}