package com.onion.ktatractcollection.Fragments.TractPictures

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onion.ktatractcollection.R
import java.io.File

class PictureItemViewHolder(
    view: View,
    private val context: Context,
    private val callbacks: PictureListCallbacks
) : RecyclerView.ViewHolder(view) {

    /**
     * Properties
     */
    private val pictureView: ImageView = view.findViewById(R.id.picture_view)

    /**
     * Methods
     */
    fun bind(photo: File) {
        Glide.with(context)
            .load(photo)
            .asBitmap()
            .centerCrop()
            .placeholder(R.drawable.ic_no_tract_photo)
            .into(pictureView)

        pictureView.setOnClickListener {
            callbacks.onPictureSelected(photo)
        }
    }

    fun bindButton() {
        pictureView.setImageResource(R.drawable.ic_add_tract_2)
        pictureView.setOnClickListener {
            callbacks.onLastButtonSelected()
        }
    }
}