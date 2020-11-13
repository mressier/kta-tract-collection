package com.unicorpdev.ktatract.Fragments.PicturesList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.unicorpdev.ktatract.R

class PicturesListAdapter(
    private val callbacks: PictureItemViewHolder.Callback
) : ListAdapter<String, PictureItemViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_pictures_item, parent, false)
        return PictureItemViewHolder(view, callbacks)
    }

    override fun onBindViewHolder(holder: PictureItemViewHolder, position: Int) {
         val item = getItem(position)
        holder.bind(item)
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
