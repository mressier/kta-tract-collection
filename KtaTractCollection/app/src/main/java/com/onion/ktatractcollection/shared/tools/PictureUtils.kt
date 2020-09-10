package com.onion.ktatractcollection.shared.tools

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View

fun decodeSampledBitmapFromFile(path: String,  view: View): Bitmap {
    return decodeSampledBitmapFromFile(path, view.width, view.height)
}

fun decodeSampledBitmapFromFile(path: String, destWidth: Int, destHeight: Int): Bitmap {
    val options = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
    }
    val sampleSize = calculateInSampleSize(options, destWidth, destHeight)

    options.inSampleSize = sampleSize
    options.inJustDecodeBounds = false

    return BitmapFactory.decodeFile(path, options)
}

fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    // Raw height and width of image
    val (height: Int, width: Int) = options.run { outHeight to outWidth }
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {

        val halfHeight: Int = height / 2
        val halfWidth: Int = width / 2

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}
