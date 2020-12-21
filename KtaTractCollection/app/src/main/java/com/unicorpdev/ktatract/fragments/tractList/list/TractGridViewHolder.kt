package com.unicorpdev.ktatract.fragments.tractList.list

import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.unicorpdev.ktatract.models.Tract
import com.unicorpdev.ktatract.models.TractPicture
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.models.TractWithPicture
import com.unicorpdev.ktatract.shared.analytics.KtaTractAnalytics
import com.unicorpdev.ktatract.shared.analytics.KtaTractAnalytics.SelectEvent
import com.unicorpdev.ktatract.shared.extensions.setTractImage
import kotlinx.android.synthetic.main.fragment_tract_grid_item.view.*

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
        setTractImage(tractItem, gridView.pictureView)
        setupListeners(tractItem)
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

    private fun setupTractImage(pictures: List<TractPicture>) {
        val picture = pictures.firstOrNull()
        val uri = picture?.let { Uri.parse(it.photoFilename) }

        Glide.with(itemView.context)
            .load(uri)
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

    private fun setupListeners(tract: TractWithPicture) {
        val tractId = tract.tract.id

        gridView.setOnClickListener { callbacks?.onTractSelected(tractId) }

        gridView.moreImageButton.setOnClickListener {
            callbacks?.onTractMoreActionsSelected(tractId)
        }

        gridView.likeImageButton.setOnClickListener {
            KtaTractAnalytics.logSelectItem(SelectEvent.LIKE)
            callbacks?.onTractToggleFavorite(tractId, !tract.tract.isFavorite)
        }

        gridView.pictureView.setOnClickListener {
            callbacks?.onTractImageSelected(0, tract)
        }
    }
}
