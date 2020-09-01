package com.onion.ktatractcollection.Fragments.TractPictures

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.onion.ktatractcollection.Fragments.TractList.TractListItem
import com.onion.ktatractcollection.R

import java.io.File

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class TractPicturesAdapter(
    private val context: Context
) : ListAdapter<File, TractPictureViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TractPictureViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_pictures_item, parent, false)
        return TractPictureViewHolder(view, context)
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
            return oldItem == newItem
        }
    }
}

class TractPictureViewHolder(view: View, context: Context) : RecyclerView.ViewHolder(view) {
    val pictureView: ImageView = view.findViewById(R.id.picture_view)

    fun bind(photo: File) {
        // bind...
    }
}