package com.unicorpdev.ktatract.fragments.collectionList.list

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.unicorpdev.ktatract.R
import java.io.File

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

    /***********************************************************************************************
     * Methods
     **********************************************************************************************/

    fun bind(item: CollectionWithPicture) {
        idView.text = item.collection.title
        contentView.text = item.collection.description

        setupImageView(item.picture)
        setupCallbacks(item)
    }

    /***********************************************************************************************
     * Setup
     **********************************************************************************************/

    private fun setupImageView(file: File?) {
        Glide.with(itemView.context)
            .load(file)
            .circleCrop()
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(imageView)
    }

    private fun setupCallbacks(item: CollectionWithPicture) {
        itemView.setOnClickListener { callbacks.onCollectionSelected(item.collection.id) }
    }

    /***********************************************************************************************
     * Override
     **********************************************************************************************/

    override fun toString(): String {
        return super.toString() + " '" + contentView.text + "'"
    }
}