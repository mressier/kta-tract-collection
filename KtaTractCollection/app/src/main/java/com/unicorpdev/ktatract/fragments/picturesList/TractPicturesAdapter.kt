package com.unicorpdev.ktatract.fragments.picturesList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.unicorpdev.ktatract.R
import java.io.File

class TractPicturesAdapter(
    private val callbacks: TractPictureViewHolder.Callback
) : ListAdapter<File, TractPictureViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TractPictureViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_pictures_item, parent, false)
        return TractPictureViewHolder(view, callbacks)
    }

    override fun onBindViewHolder(holder: TractPictureViewHolder, position: Int) {
         val item = getItem(position)
        holder.bind(item)
    }

    /**
     * Diff
     */
    class DiffUtilCallback: DiffUtil.ItemCallback<File>() {
        override fun areItemsTheSame(oldItem: File, newItem: File): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: File, newItem: File): Boolean {
            return oldItem.name == newItem.name
        }
    }
}
