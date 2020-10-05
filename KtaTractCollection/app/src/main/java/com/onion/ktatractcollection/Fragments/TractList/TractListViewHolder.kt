package com.onion.ktatractcollection.Fragments.TractList

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.R
import java.text.DateFormat
import java.util.*

class TractListViewHolder(
    view: View,
    private val callbacks: TractListCallbacks?
) : RecyclerView.ViewHolder(view) {

    /**
     * Properties
     */
    private val authorText: TextView = view.findViewById(R.id.content_text)
    private val discoveryDateText: TextView = view.findViewById(R.id.discovery_date_text)
    private val datingText: TextView = view.findViewById(R.id.dating_text)
    private val pictureView: ImageView = view.findViewById(R.id.tract_image_view)
    private val commentsTextView: TextView = view.findViewById(R.id.tract_comment_text)
    private val likeImageButton: ImageButton = view.findViewById(R.id.like_image_button)

    private val dateInstance: DateFormat by lazy {
        DateFormat.getDateInstance(DateFormat.SHORT)
    }

    override fun toString(): String {
        return super.toString() + " '" + authorText.text + "'"
    }

    /**
     * Bind
     */

    fun bind(tractItem: TractWithPicture) {
        setupTractImage(tractItem)
        setupTract(tractItem.tract)
        setupListeners(tractItem.tract)
    }

    private fun setupTract(tract: Tract) {
        setupAuthor(tract.author)
        setupComment(tract.comment)
        setupDiscoveryDate(tract.discoveryDate)
        setupDatingDate(tract.dating)
        setupLikeButton(tract.isFavorite)
    }

    private fun setupAuthor(author: String) {
        authorText.apply {
            text = if (author.isBlank()) { itemView.context.getString(R.string.unknown) } else { author }
        }
    }

    private fun setupComment(comment: String) {
        commentsTextView.apply {
            text = comment
        }
    }

    private fun setupDiscoveryDate(date: Date) {
        discoveryDateText.text = itemView.context.getString(R.string.tract_found_on)
            .format(dateInstance.format(date))
    }

    private fun setupDatingDate(date: Date?) {
        date?.let {
            datingText.text =
                itemView.context.getString(R.string.tract_dating_from).format(dateInstance.format(it))
        } ?: run { datingText.text = "" }
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

    private fun setupTractImage(tractItem: TractWithPicture) {
        val picture = tractItem.pictures.firstOrNull() ?: run { return }

        Glide.with(itemView.context)
            .load(Uri.parse(picture.photoFilename))
            .centerCrop()
            .placeholder(R.drawable.ic_no_tract_photo)
            .into(pictureView)
    }

}