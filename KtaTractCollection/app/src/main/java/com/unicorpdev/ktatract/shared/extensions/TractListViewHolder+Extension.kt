package com.unicorpdev.ktatract.shared.extensions

import android.net.Uri
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.unicorpdev.ktatract.Fragments.TractList.TractWithPicture
import com.unicorpdev.ktatract.R

fun RecyclerView.ViewHolder.setTractImage(tractItem: TractWithPicture, intoView: ImageView) {
    val picture = tractItem.pictures.firstOrNull()
    val uri = picture?.let { Uri.parse(it.photoFilename) }

    Glide.with(itemView.context)
        .load(uri)
        .centerCrop()
        .placeholder(R.drawable.ic_no_tract_photo)
        .into(intoView)
}