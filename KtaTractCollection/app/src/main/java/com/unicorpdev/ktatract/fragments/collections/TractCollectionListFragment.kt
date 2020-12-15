package com.unicorpdev.ktatract.fragments.collections

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.models.TractCollection
import kotlinx.android.synthetic.main.fragment_tract_collection_list.*
import java.util.*

/**
 * A fragment representing a list of Items.
 */
class TractCollectionListFragment : Fragment(), TractCollectionCallback {

    private val viewModel by viewModels<TractCollectionViewModel>()

    private lateinit var adapter: TractCollectionListAdapter

    /***********************************************************************************************
     * View Life Cycle
     **********************************************************************************************/
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tract_collection_list, container, false)
        
        (view as? RecyclerView)?.let { setupRecyclerView(it) }
        
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
    }
    
    /***********************************************************************************************
     * Setup
     **********************************************************************************************/

    private fun setupRecyclerView(view: RecyclerView) {
        adapter = TractCollectionListAdapter(this)

        view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@TractCollectionListFragment.adapter
        }
    }

    private fun setupObserver() {
        viewModel.collections.observe(viewLifecycleOwner) { list ->
            val noIdCollection = TractCollection(title = "Unclassified", description = "Tracts with no collection")
            val fakeCollections = listOf(
                TractCollection(title = "My Collection (Onion)", description = "My personnal collection that i have done by myself"),
                TractCollection(title = "Bug's collection", description = "The super ultimate collection of Mr. Bug."),
                TractCollection(title = "Secret collection", description = "A collection with really rare tracts...")
            )
            adapter.submitList(listOf(noIdCollection) + fakeCollections + list)
            adapter.notifyDataSetChanged()
        }
    }


    override fun onCollectionSelected(collectionId: UUID) {
        println("collection selected : $collectionId")
    }

    /***********************************************************************************************
     * Companion
     **********************************************************************************************/
    
    companion object {

        @JvmStatic
        fun newInstance() = TractCollectionListFragment()
    }

}