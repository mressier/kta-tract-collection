package com.unicorpdev.ktatract.fragments.collectionList.list

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.models.TractCollection
import kotlinx.android.synthetic.main.fragment_tract_collection_list.*
import java.util.*

/**
 * A fragment representing a list of Items.
 */
class CollectionListFragment : Fragment(), TractCollectionCallback {

    interface Callbacks {
        fun onItemCountChanged(itemCount: Int)
        fun onSelectCollection(collectionId: UUID)
        fun onSelectMoreActions(collectionId: UUID)
    }

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private val viewModel by viewModels<TractCollectionViewModel>()

    private lateinit var adapter: TractCollectionListAdapter

    private var callbacks: Callbacks? = null

    /***********************************************************************************************
     * View Life Cycle
     **********************************************************************************************/
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.fragment_tract_collection_list, container, false)

        setupViewModel()
        (view as? RecyclerView)?.let { setupRecyclerView(it) }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
    }

    override fun onAttach(context: Context) {
        callbacks = context as? Callbacks ?: parentFragment as? Callbacks
        super.onAttach(context)
    }

    override fun onDetach() {
        callbacks = null
        super.onDetach()
    }

    /***********************************************************************************************
     * Setup
     **********************************************************************************************/

    private fun setupRecyclerView(view: RecyclerView) {
        adapter = TractCollectionListAdapter(this)

        view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@CollectionListFragment.adapter
        }
    }

    private fun setupViewModel() {
        viewModel.context = requireContext()
    }

    private fun setupObserver() {
        viewModel.collections.observe(viewLifecycleOwner) { list ->
            val collectionsWithPicture = viewModel.getCollectionsWithPicture(list)
            adapter.submitList(collectionsWithPicture)
            adapter.notifyDataSetChanged()
            callbacks?.onItemCountChanged(collectionsWithPicture.size)
        }
    }

    override fun onSelectCollection(collectionId: UUID) {
        callbacks?.onSelectCollection(collectionId)
    }

    override fun onSelectMoreActions(collectionId: UUID) {
        callbacks?.onSelectMoreActions(collectionId)
    }

    /***********************************************************************************************
     * Companion
     **********************************************************************************************/
    
    companion object {

        @JvmStatic
        fun newInstance() = CollectionListFragment()
    }

}