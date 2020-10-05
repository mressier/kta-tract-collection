package com.onion.ktatractcollection.Fragments.TractList

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
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
    ) : ListAdapter<TractWithPicture, RecyclerView.ViewHolder>(DiffUtilCallback()) {

    var parameters = TractListParameters()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutId = getTractItemLayoutId()
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)

        return getTractViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        if (parameters.isList) {
            (holder as? TractListViewHolder)?.bind(item)
        } else {
            (holder as? TractGridViewHolder)?.bind(item)
        }
    }

    /**
     * View Holder
     */

    private fun getTractItemLayoutId(): Int {
        return if (parameters.isList) {
            R.layout.fragment_tract_list_item
        } else {
            R.layout.fragment_tract_grid_item
        }
    }

    private fun getTractViewHolder(view: View): RecyclerView.ViewHolder {
        return if (parameters.isList) {
            TractListViewHolder(view, context, callbacks)
        } else {
            TractGridViewHolder(view, context, callbacks)
        }
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
                    && oldItem.pictures.firstOrNull()?.id == newItem.pictures.firstOrNull()?.id
        }
    }
}