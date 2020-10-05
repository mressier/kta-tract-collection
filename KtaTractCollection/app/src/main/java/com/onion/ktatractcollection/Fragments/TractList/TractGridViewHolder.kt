package com.onion.ktatractcollection.Fragments.TractList

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.R

class TractGridViewHolder(
    view: View,
    private val context: Context,
    private val callbacks: TractListCallbacks?
) : RecyclerView.ViewHolder(view) {

    /**
     * Properties
     */
    private val pictureView: ImageView = view.findViewById(R.id.tract_image_view)
    private val contentTextView: TextView = view.findViewById(R.id.content_text)

    /**
     * Methods
     */

    fun bind(tractItem: TractWithPicture) {
        setupTract(tractItem.tract)
        setupTractImage(tractItem)
    }

    /**
     * Setup
     */

    private fun setupTract(tract: Tract) {
        val content = tract.author

        contentTextView.apply {
            text = if (content.isBlank()) { context.getString(R.string.unknown) } else { content }
        }
    }

    private fun setupTractImage(tractItem: TractWithPicture) {
        val picture = tractItem.pictures.firstOrNull() ?: run { return }

        Glide.with(itemView.context)
            .load(Uri.parse(picture.photoFilename))
            .centerCrop()
            .placeholder(R.drawable.ic_no_tract_photo)
            .into(pictureView)
    }
}