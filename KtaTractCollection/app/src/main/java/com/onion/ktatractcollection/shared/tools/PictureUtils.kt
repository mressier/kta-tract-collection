package com.onion.ktatractcollection.shared.tools

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.view.View
import kotlin.math.roundToInt

// From Android Programming book
fun getScaledBitmap(path: String, activity: Activity): Bitmap {
    val size = Point()
    activity.windowManager.defaultDisplay.getSize(size)

    return getScaledBitmap(path, size.x, size.y)
}

fun getScaledBitmap(path: String, view: View): Bitmap {
    return getScaledBitmap(path, view.width, view.height)
}

fun getScaledBitmap(path: String, destWidth: Int, destHeight: Int): Bitmap {

    // Read in the dimensions of the image on disk
    var options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(path, options)

    val srcWidth = options.outWidth.toFloat()
    val srcHeight = options.outHeight.toFloat()

    // Figure out how much to scale down by
    var inSampleSize = 1
    if (srcHeight > destHeight || srcWidth > destWidth) {
        val heightScale = srcHeight / destHeight
        val widthScale = srcWidth / destWidth
        val sampleScale = if (heightScale > widthScale) { heightScale } else { widthScale }
        inSampleSize = sampleScale.roundToInt()
    }
    options = BitmapFactory.Options()
    options.inSampleSize = inSampleSize

    // Read in and create final bitmap
    return BitmapFactory.decodeFile(path, options)
}

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

