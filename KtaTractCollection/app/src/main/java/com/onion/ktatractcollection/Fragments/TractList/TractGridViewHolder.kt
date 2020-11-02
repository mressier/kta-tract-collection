package com.onion.ktatractcollection.Fragments.TractList

import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.R
import kotlinx.android.synthetic.main.fragment_tract_grid_item.view.authorText
import kotlinx.android.synthetic.main.fragment_tract_grid_item.view.likeImageButton
import kotlinx.android.synthetic.main.fragment_tract_grid_item.view.pictureView

/**
 * TractGridViewHolder
 *
 * In the tract list, this is the view holder of each tract on "Grid" mode
 */
class TractGridViewHolder(
    private val gridView: View,
    private val callbacks: TractListCallbacks?
) : RecyclerView.ViewHolder(gridView) {

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
        gridView.authorText.apply {
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
            .into(gridView.pictureView)
    }

    private fun setupLikeButton(isFavorite: Boolean) {
        gridView.likeImageButton.apply {
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

    /**
     * Listeners
     */

    private fun setupListeners(tract: Tract) {
        itemView.setOnClickListener { callbacks?.onTractSelected(tract.id) }
        itemView.setOnLongClickListener {
            callbacks?.onTractLongSelected(tract.id)
            true
        }

        gridView.likeImageButton.setOnClickListener {
            callbacks?.onTractLikeToggled(tract.id)
        }

        gridView.pictureView.setOnClickListener {
            callbacks?.onTractImageSelected(0, tract.id)
        }
        gridView.pictureView.setOnLongClickListener {
            callbacks?.onTractLongSelected(tract.id)
            true
        }
    }
}