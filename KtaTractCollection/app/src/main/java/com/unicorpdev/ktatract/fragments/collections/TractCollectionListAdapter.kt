package com.unicorpdev.ktatract.fragments.collections

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.unicorpdev.ktatract.R

import com.unicorpdev.ktatract.models.TractCollection
import com.unicorpdev.ktatract.models.TractWithPicture
import java.util.*

interface TractCollectionCallback {
    fun onCollectionSelected(collectionId: UUID)
}

class TractCollectionListAdapter(
    val callbacks: TractCollectionCallback
): ListAdapter<TractCollection, TractCollectionListAdapter.ViewHolder>(DiffUtilCallback()) {

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
        holder.idView.text = item.title
        holder.contentView.text = item.description
        holder.itemView.setOnClickListener { callbacks.onCollectionSelected(item.id) }
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
    class DiffUtilCallback: DiffUtil.ItemCallback<TractCollection>() {
        override fun areItemsTheSame(oldItem: TractCollection, newItem: TractCollection): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TractCollection, newItem: TractCollection): Boolean {
            return oldItem == newItem
        }
    }
}