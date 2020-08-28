package com.onion.ktatractcollection.Fragments.TractList

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.onion.ktatractcollection.Fragments.TractList.TractListAdapter.TractViewHolder
import com.onion.ktatractcollection.R

import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.shared.tools.bindPhotoFromFile
import java.io.File
import java.text.DateFormat
import java.util.*

interface TractListCallbacks {
    fun onTractSelected(tractId: UUID)
    fun onTractLongSelected(tractId: UUID)
}

/**
 * [RecyclerView.Adapter] that can display a [Tract].
 */
class TractListAdapter(
    private val callbacks: TractListCallbacks?
) : ListAdapter<TractListItem, TractViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TractViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_tract_list_item, parent, false)
        return TractViewHolder(view)
    }

    override fun onBindViewHolder(holder: TractViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)
    }

    /**
     * View Holder
     */
    inner class TractViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        /**
         * Properties
         */
        private val authorText: TextView = view.findViewById(R.id.author_text)
        private val dateText: TextView = view.findViewById(R.id.date_text)
        private val pictureView: ImageView = view.findViewById(R.id.image_view)

        override fun toString(): String {
            return super.toString() + " '" + authorText.text + "'"
        }

        /**
         * Bind
         */

        fun bind(tractItem: TractListItem) {
            updateTract(tractItem.tract)
            updateTractImage(tractItem.photoFile)
        }

        private fun updateTract(tract: Tract) {
            authorText.text = tract.author
            dateText.text = DateFormat.getDateInstance(DateFormat.SHORT).format(tract.discoveryDate)
            setupClickListener(tract)
        }

        private fun setupClickListener(tract: Tract) {
            itemView.setOnClickListener { callbacks?.onTractSelected(tract.id) }
            itemView.setOnLongClickListener {
                callbacks?.onTractLongSelected(tract.id)
                true
            }
        }

        private fun updateTractImage(photo: File) {
            pictureView.viewTreeObserver.addOnGlobalLayoutListener {
                pictureView.bindPhotoFromFile(photo, R.drawable.ic_launcher_foreground)
                pictureView.setBackgroundResource(R.color.colorPrimaryDark)
                pictureView.viewTreeObserver.removeOnGlobalLayoutListener { }
            }
        }
    }

    /**
     * Diff
     */
    class DiffUtilCallback: DiffUtil.ItemCallback<TractListItem>() {
        override fun areItemsTheSame(oldItem: TractListItem, newItem: TractListItem): Boolean {
            return oldItem.tract.id == newItem.tract.id
        }

        override fun areContentsTheSame(oldItem: TractListItem, newItem: TractListItem): Boolean {
            return oldItem.tract == newItem.tract
        }
    }
}