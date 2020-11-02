package com.onion.ktatractcollection.Fragments.TractList

import android.net.Uri
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.R

class TractGridViewHolder(
    view: View,
    private val callbacks: TractListCallbacks?
) : RecyclerView.ViewHolder(view) {

    /**
     * Properties
     */
    private val pictureView: ImageView = view.findViewById(R.id.tractImageView)
    private val contentTextView: TextView = view.findViewById(R.id.content_text)
    private val likeImageButton: ImageButton = view.findViewById(R.id.like_image_button)

    /**
     * Methods
     */

    fun bind(tractItem: TractWithPicture) {
        setupTract(tractItem.tract)
        setupTractImage(tractItem)
        setupListeners(tractItem.tract)
    }

    /**
     * Setup
     */

    private fun setupTract(tract: Tract) {
        val content = tract.author
        contentTextView.apply {
            text = if (content.isBlank()) { context.getString(R.string.unknown) } else { content }
        }
        setupLikeButton(tract.isFavorite)
    }

    private fun setupTractImage(tractItem: TractWithPicture) {
        val picture = tractItem.pictures.firstOrNull() ?: run { return }

        Glide.with(itemView.context)
            .load(Uri.parse(picture.photoFilename))
            .centerCrop()
            .placeholder(R.drawable.ic_no_tract_photo)
            .into(pictureView)
    }

    private fun setupLikeButton(isFavorite: Boolean) {
        likeImageButton.apply {
            val resource = if (isFavorite) {
                R.drawable.ic_baseline_star_24
            } else {
                R.drawable.ic_baseline_star_border_24
            }

            val descriptionId = if (isFavorite) {
                R.string.btn_desc_do_not_like_action
            } else {
                R.string.btn_desc_like_action
            }
            setImageResource(resource)
            contentDescription = itemView.context.getString(descriptionId)
        }
    }

    private fun setupListeners(tract: Tract) {
        itemView.setOnClickListener { callbacks?.onTractSelected(tract.id) }
        itemView.setOnLongClickListener {
            callbacks?.onTractLongSelected(tract.id)
            true
        }

        likeImageButton.setOnClickListener {
            callbacks?.onTractLikeToggled(tract.id)
        }
    }
}