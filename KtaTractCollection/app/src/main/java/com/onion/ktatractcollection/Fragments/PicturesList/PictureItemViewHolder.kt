package com.onion.ktatractcollection.Fragments.PicturesList

import android.net.Uri
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onion.ktatractcollection.R

interface PictureItemCallback {
    fun onPictureSelected(path: String)
    fun onDeleteButtonSelected(path: String)
}

/**
 * PictureItemViewHolder
 */
class PictureItemViewHolder(
    view: View,
    private val callbacks: PictureItemCallback
) : RecyclerView.ViewHolder(view) {

    /**
     * Properties
     */
    private val pictureView: ImageView = view.findViewById(R.id.picture_view)
    private val deleteButton: ImageButton = view.findViewById(R.id.delete_button)

    /**
     * Methods
     */
    fun bind(photoPath: String) {
        setupPhoto(photoPath)
        setupListeners(photoPath)
        setDeleteButtonIsVisible(true)
    }

    private fun setupPhoto(path: String) {
        Glide.with(itemView.context)
            .load(Uri.parse(path))
            .dontAnimate()
            .centerCrop()
            .placeholder(R.drawable.ic_no_tract_photo)
            .into(pictureView)
    }

    private fun setupListeners(path: String) {
        pictureView.setOnClickListener {
            callbacks.onPictureSelected(path)
        }

        deleteButton.setOnClickListener {
            callbacks.onDeleteButtonSelected(path)
            setDeleteButtonIsVisible(false)
        }
    }

    private fun setDeleteButtonIsVisible(isVisible: Boolean) {
        deleteButton.visibility = if (isVisible) { View.VISIBLE } else { View.GONE }
    }
}