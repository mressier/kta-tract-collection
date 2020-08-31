package com.onion.ktatractcollection.shared.tools

import android.widget.ImageView
import com.onion.ktatractcollection.R
import java.io.File

fun ImageView.bindPhotoFromFile(file: File, defaultResourceId: Int) {
    if (file.exists()) {
        val bitmap = decodeSampledBitmapFromFile(file.path, this)
        this.setImageBitmap(bitmap)
    } else {
        this.setImageResource(defaultResourceId)
    }
}