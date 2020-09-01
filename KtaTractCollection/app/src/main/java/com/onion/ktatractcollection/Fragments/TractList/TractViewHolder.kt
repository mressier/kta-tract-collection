package com.onion.ktatractcollection.Fragments.TractList

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.R
import java.io.File
import java.text.DateFormat

class TractViewHolder(
    view: View,
    private val context: Context,
    private val callbacks: TractListCallbacks?
) : RecyclerView.ViewHolder(view) {

    /**
     * Properties
     */
    private val authorText: TextView = view.findViewById(R.id.author_text)
    private val dateText: TextView = view.findViewById(R.id.date_text)
    private val pictureView: ImageView = view.findViewById(R.id.image_view)

    override fun toString(): String {
        return super.toString() + " '" + authorText.text + "'"
    }
    /**
     * Bind
     */

    fun bind(tractItem: TractListItem) {
        updateTract(tractItem.tract)
        updateTractImage(tractItem.photoFile)
    }

    private fun updateTract(tract: Tract) {
        authorText.text = tract.author
        authorText.visibility = if (tract.author.isBlank()) { View.GONE } else { View.VISIBLE }
        dateText.text = DateFormat.getDateInstance(DateFormat.SHORT).format(tract.discoveryDate)
        setupClickListener(tract)
    }

    private fun setupClickListener(tract: Tract) {
        itemView.setOnClickListener { callbacks?.onTractSelected(tract.id) }
        itemView.setOnLongClickListener {
            callbacks?.onTractLongSelected(tract.id)
            true
        }
    }

    private fun updateTractImage(photo: File) {
        Glide.with(context)
            .load(photo)
            .asBitmap()
            .centerCrop()
            .placeholder(R.drawable.ic_no_tract_photo)
            .into(pictureView)
    }

}