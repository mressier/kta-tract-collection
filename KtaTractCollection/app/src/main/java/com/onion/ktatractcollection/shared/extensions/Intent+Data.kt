package com.onion.ktatractcollection.shared.extensions

import android.content.Intent
import android.net.Uri

/**
 * Return an array of [Uri] from clipData
 */
fun Intent.clipDataAsUriArray(): Array<Uri> {
    return clipData?.let { clipData ->
        return (0 until clipData.itemCount).map {
            clipData.getItemAt(it).uri
        }.toTypedArray()
    } ?: run { arrayOf() }
}

/**
 * Return an array of [Uri] from data or clipData
 */
fun Intent.dataAsUriArray(): Array<Uri> {
    return data?.let { arrayOf(it) } ?: clipDataAsUriArray()
}