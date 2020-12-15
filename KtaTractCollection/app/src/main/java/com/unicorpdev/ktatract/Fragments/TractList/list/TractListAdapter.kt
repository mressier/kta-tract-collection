package com.unicorpdev.ktatract.Fragments.TractList.list

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.unicorpdev.ktatract.Fragments.TractList.parameters.DisplayMode
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.models.Tract
import com.unicorpdev.ktatract.models.TractWithPicture
import java.util.*

interface TractListCallbacks {
    fun onTractSelected(tractId: UUID)
    fun onTractLongSelected(tractId: UUID)
    fun onTractToggleFavorite(tractId: UUID, isFavorite: Boolean)
    fun onTractImageSelected(imageIndex: Int, tract: TractWithPicture)
    fun onItemCountChanged(numberOfItems: Int)
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
    var displayMode = DisplayMode.LIST

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

        if (displayMode == DisplayMode.LIST) {
            (holder as? TractListViewHolder)?.bind(item)
        } else {
            (holder as? TractGridViewHolder)?.bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (displayMode == DisplayMode.LIST) {
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
        return if (displayMode == DisplayMode.LIST) {
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
            return oldItem.tract == newItem.tract
                    && oldItem.pictures.map { it.id } == newItem.pictures.map { it.id }
        }
    }
}