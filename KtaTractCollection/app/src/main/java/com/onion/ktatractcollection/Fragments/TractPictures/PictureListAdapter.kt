package com.onion.ktatractcollection.Fragments.TractPictures

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.onion.ktatractcollection.R

import java.io.File

class PictureListAdapter(
    private val context: Context,
    private val callbacks: PictureListCallbacks
) : ListAdapter<File, PictureItemViewHolder>(DiffUtilCallback()) {

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
    class DiffUtilCallback: DiffUtil.ItemCallback<File>() {
        override fun areItemsTheSame(oldItem: File, newItem: File): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: File, newItem: File): Boolean {
            return oldItem == newItem
        }
    }
}
