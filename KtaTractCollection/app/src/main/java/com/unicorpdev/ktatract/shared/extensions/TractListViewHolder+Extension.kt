package com.unicorpdev.ktatract.shared.extensions

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.models.TractWithPicture
import java.io.File

fun RecyclerView.ViewHolder.setTractImage(tractItem: TractWithPicture, intoView: ImageView) {
    val picture = tractItem.pictures.firstOrNull()
    val file = picture?.let { tractItem.picturesFile[it.id] }

    Glide.with(itemView.context)
        .load(file)
        .centerCrop()
        .placeholder(R.drawable.ic_no_tract_photo)
        .into(intoView)
}