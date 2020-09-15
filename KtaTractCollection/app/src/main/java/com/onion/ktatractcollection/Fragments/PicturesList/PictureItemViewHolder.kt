package com.onion.ktatractcollection.Fragments.PicturesList

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.net.toFile
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.RequestManager
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
    fun bind(photoPath: String) {
        println("-------- bind $photoPath")
        setupPhoto(photoPath)
        setupListeners(photoPath)
        setDeleteButtonIsVisible(true)
    }

    fun bindButton() {
        pictureView.setImageResource(R.drawable.ic_add_tract_2)
        pictureView.setOnClickListener {
            callbacks.onLastButtonSelected()
        }

        setDeleteButtonIsVisible(false)
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
        }
    }

    private fun setDeleteButtonIsVisible(isVisible: Boolean) {
        deleteButton.visibility = if (isVisible) { View.VISIBLE } else { View.GONE }
    }
}