package com.unicorpdev.ktatract.fragments.collections.list

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.unicorpdev.ktatract.R

import kotlinx.android.synthetic.main.fragment_tract_collection_item.view.*
import java.io.File
import java.util.*

interface TractCollectionCallback {
    fun onCollectionSelected(collectionId: UUID)
}

class TractCollectionListAdapter(
    val callbacks: TractCollectionCallback
): ListAdapter<CollectionWithPicture, TractCollectionViewHolder>(DiffUtilCallback()) {

    /***********************************************************************************************
     * View Life Cycle
     **********************************************************************************************/
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TractCollectionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_tract_collection_item, parent, false)
        return TractCollectionViewHolder(view, callbacks)
    }
    
    /***********************************************************************************************
     * Bind
     **********************************************************************************************/

    override fun onBindViewHolder(holder: TractCollectionViewHolder, position: Int) {
        val item = getItem(position)

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