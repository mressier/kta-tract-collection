package com.onion.ktatractcollection.Fragments.PicturesList

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.onion.ktatractcollection.R

class PicturesListAdapter(
    private val context: Context,
    private val callbacks: PictureListCallbacks
) : ListAdapter<String, PictureItemViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_pictures_item, parent, false)
        return PictureItemViewHolder(view, context, callbacks)
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }


    override fun onBindViewHolder(holder: PictureItemViewHolder, position: Int) {
        if (position == itemCount - 1) {
            holder.bindButton()
        } else {
            val item = getItem(position)
            holder.bind(item)
        }
    }

    /**
     * Diff
     */
    class DiffUtilCallback: DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem.contentEquals(newItem)
        }
    }
}
