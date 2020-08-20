package com.onion.ktatractcollection.Fragments.TractList

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ListAdapter
import com.onion.ktatractcollection.R
import com.onion.ktatractcollection.Models.Tract
import java.util.*

/**
 * A fragment representing a list of Items.
 */
class TractListFragment : Fragment() {

    /**
     * Required interface for hosting activities
     */
    interface Callbacks {
        fun onTractSelected(tractId: UUID)
    }

    /**
     * Properties
     */

    private var callbacks: Callbacks? = null
    private val tractListViewModel: TractListViewModel by lazy {
        ViewModelProvider(this).get(TractListViewModel::class.java)
    }

    private lateinit var tractRecyclerView: RecyclerView
    private lateinit var tractAdapter: TractListAdapter

    /**
     * View Life Cycle
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tract_list, container, false)
        setupRecyclerView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModelObserver()
    }

    private fun setupViewModelObserver() {
        tractListViewModel.tracts.observe(
            viewLifecycleOwner,
            { items ->
                items?.let {
                    Log.i("TAG", "Got items ${items.size}")
                    updateUI(items)
                }
            }
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as? Callbacks
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.tract_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.new_tract -> {
                val tract = Tract()
                tractListViewModel.saveTract(tract)
                callbacks?.onTractSelected(tract.id)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Update
     */
    private fun updateUI(tracts: List<Tract>) {
        tractAdapter.submitList(tracts)
        tractAdapter.notifyDataSetChanged()
    }

    /**
     * Setup
     */

    private fun setupRecyclerView(view: View) {
        tractRecyclerView = view.findViewById(R.id.tract_list)
        tractAdapter = TractListAdapter(callbacks)

        tractRecyclerView.adapter = tractAdapter
        tractRecyclerView.layoutManager = GridLayoutManager(context, 2)

    }

}