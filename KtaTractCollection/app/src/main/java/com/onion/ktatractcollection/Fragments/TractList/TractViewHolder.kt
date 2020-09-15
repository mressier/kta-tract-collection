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
import java.text.DateFormat
import java.util.*

class TractViewHolder(
    view: View,
    private val context: Context,
    private val callbacks: TractListCallbacks?
) : RecyclerView.ViewHolder(view) {

    /**
     * Properties
     */
    private val authorText: TextView = view.findViewById(R.id.author_text)
    private val discoveryDateText: TextView = view.findViewById(R.id.discovery_date_text)
    private val datingText: TextView = view.findViewById(R.id.dating_text)
    private val pictureView: ImageView = view.findViewById(R.id.image_view)
    private val separatorView: ImageView = view.findViewById(R.id.separator_view)

    private val dateInstance: DateFormat by lazy {
        DateFormat.getDateInstance(DateFormat.SHORT)
    }

    override fun toString(): String {
        return super.toString() + " '" + authorText.text + "'"
    }

    /**
     * Bind
     */

    fun bind(tractItem: TractWithPicture, showTractDetails: Boolean) {
        updateTractImage(tractItem)
        updateTract(tractItem.tract)
        setTractDetailsAsVisible(showTractDetails)
        setupClickListener(tractItem.tract)
    }

    private fun updateTract(tract: Tract) {
        authorText.text = tract.author
        authorText.visibility = if (tract.author.isBlank()) { View.GONE } else { View.VISIBLE }

        setDiscoveryDate(tract.discoveryDate)
        setDatingDate(tract.dating)
    }

    private fun setDiscoveryDate(date: Date) {
        discoveryDateText.text = itemView.context.getString(R.string.tract_found_on)
            .format(dateInstance.format(date))
    }

    private fun setDatingDate(date: Date?) {
        date?.let {
            datingText.text =
                context.getString(R.string.tract_dating_from).format(dateInstance.format(it))
        } ?: run { datingText.text = "" }
    }

    private fun setTractDetailsAsVisible(isVisible: Boolean) {
        val visibility = if (isVisible) { View.VISIBLE } else { View.GONE }
        authorText.visibility = visibility
        discoveryDateText.visibility = visibility
        datingText.visibility = visibility
        separatorView.visibility = visibility
    }

    private fun setupClickListener(tract: Tract) {
        itemView.setOnClickListener { callbacks?.onTractSelected(tract.id) }
        itemView.setOnLongClickListener {
            callbacks?.onTractLongSelected(tract.id)
            true
        }
    }

    private fun updateTractImage(tractItem: TractWithPicture) {
        val picture = tractItem.pictures.firstOrNull() ?: run { return }

        Glide.with(itemView.context)
            .load(Uri.parse(picture.photoFilename))
            .centerCrop()
            .placeholder(R.drawable.ic_no_tract_photo)
            .into(pictureView)
    }

}