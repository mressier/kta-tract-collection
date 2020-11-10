package com.onion.ktatractcollection.Fragments.PicturesList

import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onion.ktatractcollection.R
import kotlinx.android.synthetic.main.fragment_pictures_item.view.*

/**
 * PictureItemViewHolder
 */
class PictureItemViewHolder(
    val view: View,
    private val callbacks: Callback
) : RecyclerView.ViewHolder(view) {

    interface Callback {
        fun onPictureSelected(path: String)
        fun onDeleteButtonSelected(path: String)
    }
    /**
     * Methods
     */

    fun bind(photoPath: String) {
        setupPhoto(photoPath)
        setupListeners(photoPath)
        setDeleteButtonIsVisible(true)
    }

    /**
     * Setup
     */
    private fun setupPhoto(path: String) {
        Glide.with(itemView.context)
            .load(Uri.parse(path))
            .dontAnimate()
            .centerCrop()
            .placeholder(R.drawable.ic_no_tract_photo)
            .into(view.pictureImageView)
    }

    private fun setupListeners(path: String) {
        view.pictureImageView.setOnClickListener {
            callbacks.onPictureSelected(path)
        }

        view.deleteButton.setOnClickListener {
            callbacks.onDeleteButtonSelected(path)
            setDeleteButtonIsVisible(false)
        }
    }

    private fun setDeleteButtonIsVisible(isVisible: Boolean) {
        view.deleteButton.visibility = if (isVisible) { View.VISIBLE } else { View.GONE }
    }
}