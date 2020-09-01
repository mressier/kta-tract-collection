package com.onion.ktatractcollection.Fragments.TractList

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.onion.ktatractcollection.R
import com.onion.ktatractcollection.Models.Tract
import java.util.*

interface TractListCallbacks {
    fun onTractSelected(tractId: UUID)
    fun onTractLongSelected(tractId: UUID)
}

/**
 * [RecyclerView.Adapter] that can display a [Tract].
 */
class TractListAdapter(
    private val context: Context,
    private val callbacks: TractListCallbacks?,
    ) : ListAdapter<TractListItem, TractViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TractViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_tract_list_item, parent, false)
        return TractViewHolder(view, context, callbacks)
    }

    override fun onBindViewHolder(holder: TractViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
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