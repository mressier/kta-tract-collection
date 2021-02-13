package com.unicorpdev.ktatract.fragments.collectionList.list

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.models.TractCollection
import kotlinx.android.synthetic.main.fragment_tract_collection_item.view.*
import java.io.File
import java.util.*

class CollectionListItemViewHolder(
    view: View,
    val callbacks: TractCollectionCallback
) : RecyclerView.ViewHolder(view) {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private val idView: TextView = view.findViewById(R.id.collectionTitleText)
    private val contentView: TextView = view.findViewById(R.id.collectionDescriptionText)
    private val imageView: ImageView = view.findViewById(R.id.collectionImageView)
    private val moreImageButton: ImageView = view.findViewById(R.id.moreImageButton)

    /***********************************************************************************************
     * Methods
     **********************************************************************************************/

    fun bind(item: CollectionWithPicture) {
        setupTitle(item.collection)
        setupDescription(item.collection)
        setupImageView(item.picture)
        setupButtons()
        setupListeners(item.collection.id)
    }

    /***********************************************************************************************
     * Setup
     **********************************************************************************************/

    private fun setupTitle(collection: TractCollection) {
        idView.text = if (collection.title.isEmpty()) {
            itemView.context.getString(R.string.collection_unnamed)
        } else {
            collection.title
        }
    }

    private fun setupDescription(collection: TractCollection) {
        contentView.visibility =
            if (collection.description.isEmpty()) { View.GONE } else { View.VISIBLE }
        contentView.text = collection.description
    }

    private fun setupImageView(file: File?) {
        Glide.with(itemView.context)
            .load(file)
            .circleCrop()
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(imageView)
    }

    private fun setupButtons() {
        val isVisible = true
        moreImageButton.visibility = if (isVisible) { View.VISIBLE } else { View.INVISIBLE }
    }

    private fun setupListeners(collectionId: UUID) {
        itemView.setOnClickListener { callbacks.onSelectCollection(collectionId) }
        moreImageButton.setOnClickListener { callbacks.onSelectMoreActions(collectionId) }
    }

    /***********************************************************************************************
     * Override
     **********************************************************************************************/

    override fun toString(): String {
        return super.toString() + " '" + contentView.text + "'"
    }
}