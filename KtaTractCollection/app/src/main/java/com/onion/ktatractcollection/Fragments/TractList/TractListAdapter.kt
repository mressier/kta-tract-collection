package com.onion.ktatractcollection.Fragments.TractList

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.onion.ktatractcollection.R

import com.onion.ktatractcollection.Models.Tract
import java.text.DateFormat

/**
 * [RecyclerView.Adapter] that can display a [Tract].
 * TODO: Replace the implementation with code for your data type.
 */
class TractListAdapter(
    private val values: List<Tract>,
    private val callbacks: TractListFragment.Callbacks?
) : RecyclerView.Adapter<TractListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_tract_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.authorText.text = item.author
        holder.dateText.text = DateFormat.getDateInstance(DateFormat.SHORT).format(item.discoveryDate)
        holder.itemView.setOnClickListener { callbacks?.onTractSelected(item.id) }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val authorText: TextView = view.findViewById(R.id.author_text)
        val dateText: TextView = view.findViewById(R.id.date_text)

        override fun toString(): String {
            return super.toString() + " '" + authorText.text + "'"
        }
    }
}