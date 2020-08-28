package com.onion.ktatractcollection.Fragments.TractList

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.onion.ktatractcollection.Fragments.TractList.TractListAdapter.TractViewHolder
import com.onion.ktatractcollection.R

import com.onion.ktatractcollection.Models.Tract
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
) : ListAdapter<Tract, TractViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TractViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_tract_list_item, parent, false)
        return TractViewHolder(view)
    }

    override fun onBindViewHolder(holder: TractViewHolder, position: Int) {
        val item = getItem(position)
        println(item)
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

        override fun toString(): String {
            return super.toString() + " '" + authorText.text + "'"
        }

        fun bind(tract: Tract) {
            authorText.text = tract.author
            dateText.text = DateFormat.getDateInstance(DateFormat.SHORT).format(tract.discoveryDate)
            itemView.setOnClickListener { callbacks?.onTractSelected(tract.id) }
            itemView.setOnLongClickListener {
                callbacks?.onTractLongSelected(tract.id)
                true
            }
        }
    }

    /**
     * Diff
     */
    class DiffUtilCallback: DiffUtil.ItemCallback<Tract>() {
        override fun areItemsTheSame(oldItem: Tract, newItem: Tract): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Tract, newItem: Tract): Boolean {
            return  oldItem == newItem
        }
    }
}