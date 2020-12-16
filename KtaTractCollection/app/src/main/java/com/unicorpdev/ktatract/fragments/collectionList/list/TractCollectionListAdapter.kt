package com.unicorpdev.ktatract.fragments.collectionList.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.unicorpdev.ktatract.R

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
        holder.bind(item)
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