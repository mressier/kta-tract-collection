package com.onion.ktatractcollection.Fragments.TractList

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.onion.ktatractcollection.Fragments.TractList.dialogs.TractListParameters
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
    ) : ListAdapter<TractWithPicture, TractViewHolder>(DiffUtilCallback()) {

    var parameters = TractListParameters()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TractViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_tract_list_item, parent, false)
        return TractViewHolder(view, context, callbacks)
    }

    override fun onBindViewHolder(holder: TractViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,
            parameters.displayMode == TractListParameters.DisplayMode.LIST)
    }

    /**
     * Diff
     */
    class DiffUtilCallback: DiffUtil.ItemCallback<TractWithPicture>() {
        override fun areItemsTheSame(oldItem: TractWithPicture, newItem: TractWithPicture): Boolean {
            return oldItem.tract.id == newItem.tract.id
        }

        override fun areContentsTheSame(oldItem: TractWithPicture, newItem: TractWithPicture): Boolean {
            return oldItem.tract.author == newItem.tract.author
                    && oldItem.tract.discoveryDate == newItem.tract.discoveryDate
                    && oldItem.pictures == newItem.pictures
        }
    }
}