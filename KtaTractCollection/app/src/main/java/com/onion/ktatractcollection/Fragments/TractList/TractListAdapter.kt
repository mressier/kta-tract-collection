package com.onion.ktatractcollection.Fragments.TractList

import android.content.Context
import android.util.Log
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
    fun onTractLikeToggled(tractId: UUID)
    fun onTractImageSelected(imageIndex: Int, tractId: UUID)
}

/**
 * [RecyclerView.Adapter] that can display a [Tract].
 */
class TractListAdapter(
    private val callbacks: TractListCallbacks?
) : ListAdapter<TractWithPicture, RecyclerView.ViewHolder>(DiffUtilCallback()) {

    /**
     * Properties
     */
    var parameters = TractListParameters()

    enum class ViewTypeValue(val layoutId: Int) {
        LIST(R.layout.fragment_tract_list_item),
        GRID(R.layout.fragment_tract_grid_item)
    }

    /**
     * View Life Cycle
     */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutId = getTractItemLayoutId(viewType)
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

    override fun getItemViewType(position: Int): Int {
        return if (parameters.isList) {
            ViewTypeValue.LIST.ordinal
        } else {
            ViewTypeValue.GRID.ordinal
        }
    }

    /**
     * View Holder
     */

    private fun getTractItemLayoutId(viewType: Int): Int {
        return (ViewTypeValue.values()[viewType].layoutId)
    }

    private fun getTractViewHolder(view: View): RecyclerView.ViewHolder {
        return if (parameters.isList) {
            TractListViewHolder(view, callbacks)
        } else {
            TractGridViewHolder(view, callbacks)
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
                    && oldItem.tract.dating == newItem.tract.dating
                    && oldItem.tract.isFavorite == newItem.tract.isFavorite
                    && oldItem.pictures.firstOrNull()?.id == newItem.pictures.firstOrNull()?.id
        }
    }
}