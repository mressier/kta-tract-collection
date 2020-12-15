package com.unicorpdev.ktatract.fragments.PicturesList

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.unicorpdev.ktatract.R
import kotlinx.android.synthetic.main.fragment_pictures_item.view.*
import java.io.File

/**
 * PictureItemViewHolder
 */
class TractPictureViewHolder(
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

    fun bind(photo: File) {
        setupPhoto(photo)
        setupListeners(photo)
        setDeleteButtonIsVisible(true)
    }

    /**
     * Setup
     */
    private fun setupPhoto(file: File) {
        Glide.with(itemView.context)
            .load(file)
            .dontAnimate()
            .centerCrop()
            .placeholder(R.drawable.ic_no_tract_photo)
            .into(view.pictureImageView)
    }

    private fun setupListeners(file: File) {
        view.pictureImageView.setOnClickListener {
            callbacks.onPictureSelected(file.name)
        }

        view.deleteButton.setOnClickListener {
            callbacks.onDeleteButtonSelected(file.name)
            setDeleteButtonIsVisible(false)
        }
    }

    private fun setDeleteButtonIsVisible(isVisible: Boolean) {
        view.deleteButton.visibility = if (isVisible) { View.VISIBLE } else { View.GONE }
    }
}