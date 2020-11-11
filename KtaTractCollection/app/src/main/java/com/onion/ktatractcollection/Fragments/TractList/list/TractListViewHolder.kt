package com.onion.ktatractcollection.Fragments.TractList.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.onion.ktatractcollection.Fragments.TractList.TractWithPicture
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.R
import com.onion.ktatractcollection.shared.extensions.setTractImage
import com.onion.ktatractcollection.shared.extensions.shortString
import kotlinx.android.synthetic.main.fragment_tract_list_item.view.*
import kotlinx.android.synthetic.main.fragment_tract_list_item.view.authorText
import kotlinx.android.synthetic.main.fragment_tract_list_item.view.likeImageButton
import kotlinx.android.synthetic.main.fragment_tract_list_item.view.pictureView

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

    override fun toString(): String {
        return super.toString() + " '" + listView.authorText.text + "'"
    }

    /**
     * Bind
     */

    fun bind(tractItem: TractWithPicture) {
        setTractImage(tractItem, listView.pictureView)
        setupTract(tractItem.tract)
        setupListeners(tractItem)
    }

    private fun setupTract(tract: Tract) {
        setupAuthor(tract.author)
        setupComment(tract.comment)
        setupDiscoveryDate(tract.discoveryDate.shortString)
        setupDatingDate(tract.dating?.shortString)
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

    private fun setupDiscoveryDate(date: String) {
        listView.discoveryDateText.text = itemView.context.getString(R.string.tract_found_on).format(date)
    }

    private fun setupDatingDate(date: String?) {
        date?.let {
            listView.datingText.text = itemView.context.getString(R.string.tract_dating_from).format(it)
        } ?: run { listView.datingText.text = "" }
    }

    private fun setupLikeButton(isFavorite: Boolean) {
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

        listView.likeImageButton.apply {

            setImageResource(resource)
            contentDescription = itemView.context.getString(descriptionId)
        }
    }

    private fun setupListeners(tract: TractWithPicture) {
        val tractId = tract.tract.id

        itemView.setOnClickListener { callbacks?.onTractSelected(tractId) }

        itemView.setOnLongClickListener {
            callbacks?.onTractLongSelected(tractId)
            true
        }

        listView.likeImageButton.setOnClickListener {
            callbacks?.onTractToggleFavorite(tractId, !tract.tract.isFavorite)
        }

        listView.pictureView.setOnClickListener {
            callbacks?.onTractImageSelected(0, tractId, tract.pictures)
        }

        listView.pictureView.setOnLongClickListener {
            callbacks?.onTractLongSelected(tractId)
            true
        }
    }
}
