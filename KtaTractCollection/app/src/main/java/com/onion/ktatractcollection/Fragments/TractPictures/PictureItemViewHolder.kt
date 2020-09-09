package com.onion.ktatractcollection.Fragments.TractPictures

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.view.isVisible
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
    private val deleteButton: ImageButton = view.findViewById(R.id.delete_button)

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

        deleteButton.setOnClickListener {
            callbacks.onDeleteButtonSelected(photo)
        }

        setDeleteButtonIsVisible(true)
    }

    fun bindButton() {
        pictureView.setImageResource(R.drawable.ic_add_tract_2)
        pictureView.setOnClickListener {
            callbacks.onLastButtonSelected()
        }

        setDeleteButtonIsVisible(false)
    }

    private fun setDeleteButtonIsVisible(isVisible: Boolean) {
        deleteButton.visibility = if (isVisible) { View.VISIBLE } else { View.GONE }
    }
}