package com.onion.ktatractcollection.Fragments.TractList

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.onion.ktatractcollection.Fragments.TractList.TractListAdapter.ViewHolder
import com.onion.ktatractcollection.R

import com.onion.ktatractcollection.Models.Tract
import java.text.DateFormat

/**
 * [RecyclerView.Adapter] that can display a [Tract].
 */
class TractListAdapter(
    private val callbacks: TractListFragment.Callbacks?
) : ListAdapter<Tract, ViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_tract_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        println(item)
        holder.bind(item)
    }

    /**
     * View Holder
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val authorText: TextView = view.findViewById(R.id.author_text)
        private val dateText: TextView = view.findViewById(R.id.date_text)

        override fun toString(): String {
            return super.toString() + " '" + authorText.text + "'"
        }

        fun bind(tract: Tract) {
            println("bin !")
            authorText.text = tract.author
            dateText.text = DateFormat.getDateInstance(DateFormat.SHORT).format(tract.discoveryDate)
            itemView.setOnClickListener { callbacks?.onTractSelected(tract.id) }
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