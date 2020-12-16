package com.unicorpdev.ktatract.fragments.collections

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.unicorpdev.ktatract.R

import com.unicorpdev.ktatract.models.TractCollection
import com.unicorpdev.ktatract.models.TractWithPicture
import kotlinx.android.synthetic.main.fragment_tract_collection_item.view.*
import java.io.File
import java.util.*

interface TractCollectionCallback {
    fun onCollectionSelected(collectionId: UUID)
}

class TractCollectionListAdapter(
    val callbacks: TractCollectionCallback
): ListAdapter<CollectionWithPicture, TractCollectionListAdapter.ViewHolder>(DiffUtilCallback()) {

    /***********************************************************************************************
     * View Life Cycle
     **********************************************************************************************/
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_tract_collection_item, parent, false)
        return ViewHolder(view)
    }
    
    /***********************************************************************************************
     * Bind
     **********************************************************************************************/

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.idView.text = item.collection.title
        holder.contentView.text = item.collection.description

        setupImageView(holder, item.picture)
        setupCallbacks(holder, item)
    }

    private fun setupImageView(holder: ViewHolder, file: File?) {
        Glide.with(holder.itemView.context)
            .load(file)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.itemView.collectionImageView)
    }

    private fun setupCallbacks(holder: ViewHolder, item: CollectionWithPicture) {
        holder.itemView.setOnClickListener { callbacks.onCollectionSelected(item.collection.id) }
    }
    
    /***********************************************************************************************
     * View Holder
     **********************************************************************************************/

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.item_number)
        val contentView: TextView = view.findViewById(R.id.content)

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

    /**
     * Diff
     */
    class DiffUtilCallback: DiffUtil.ItemCallback<CollectionWithPicture>() {
        override fun areItemsTheSame(oldItem: CollectionWithPicture, newItem: CollectionWithPicture): Boolean {
            return oldItem.collection.id == newItem.collection.id
        }

        override fun areContentsTheSame(oldItem: CollectionWithPicture, newItem: CollectionWithPicture): Boolean {
            return oldItem.collection.id == newItem.collection.id
                    && oldItem.picture == newItem.picture
        }
    }
}