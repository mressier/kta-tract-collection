package com.onion.ktatractcollection.Fragments.TractList

import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.R
import java.text.DateFormat
import java.util.*
import kotlinx.android.synthetic.main.fragment_tract_list_item.view.*

/**
 * TractListViewHolder
 *
 * In the tract list, this is the view holder of each tract on "List" mode
 */
class TractListViewHolder(
    private val listView: View,
    private val callbacks: TractListCallbacks?
) : RecyclerView.ViewHolder(listView) {

    /**
     * Properties
     */

    private val dateInstance: DateFormat by lazy {
        DateFormat.getDateInstance(DateFormat.SHORT)
    }

    override fun toString(): String {
        return super.toString() + " '" + listView.authorText.text + "'"
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
        listView.authorText.apply {
            text = if (author.isBlank()) { itemView.context.getString(R.string.unknown) } else { author }
        }
    }

    private fun setupComment(comment: String) {
        listView.commentsTextView.apply {
            text = comment
        }
    }

    private fun setupDiscoveryDate(date: Date) {
        listView.discoveryDateText.text = itemView.context.getString(R.string.tract_found_on)
            .format(dateInstance.format(date))
    }

    private fun setupDatingDate(date: Date?) {
        date?.let {
            listView.datingText.text =
                itemView.context.getString(R.string.tract_dating_from).format(dateInstance.format(it))
        } ?: run { listView.datingText.text = "" }
    }

    private fun setupLikeButton(isFavorite: Boolean) {
        listView.likeImageButton.apply {
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

        listView.likeImageButton.setOnClickListener {
            callbacks?.onTractLikeToggled(tract.id)
        }

        listView.pictureView.setOnClickListener {
            callbacks?.onTractImageSelected(0, tract.id)
        }
    }

    private fun setupTractImage(tractItem: TractWithPicture) {
        val picture = tractItem.pictures.firstOrNull() ?: run { return }

        Glide.with(itemView.context)
            .load(Uri.parse(picture.photoFilename))
            .centerCrop()
            .placeholder(R.drawable.ic_no_tract_photo)
            .into(listView.pictureView)
    }

}