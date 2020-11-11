package com.onion.ktatractcollection.Fragments.TractList.list

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.onion.ktatractcollection.Fragments.TractList.parameters.DisplayMode
import com.onion.ktatractcollection.Fragments.TractList.parameters.TractListParameters
import com.onion.ktatractcollection.R
import kotlinx.android.synthetic.main.fragment_tract_recycler.*

/**
 * A [Fragment] subclass to display a list of tracts
 * Use the [TractListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TractListFragment : Fragment() {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private val viewModel: TractListViewModel by lazy {
        ViewModelProvider(this).get(TractListViewModel::class.java)
    }

    private var callbacks: TractListCallbacks? = null

    /** Fragment parameters **/

    var displayMode: DisplayMode
        get() = viewModel.displayMode
        set(value) {
            viewModel.displayMode = value
            updateLayout()
        }

    var parameters: TractListParameters
        get() = viewModel.listParameters
        set(value) {
            viewModel.listParameters = value
            updateContent()
        }

    var searchText: String
        get() = viewModel.listParameters.searchText
        set(value) {
            viewModel.listParameters.searchText = value
            updateContent()
        }

    /** Computed **/

    val itemCount: Int
        get() = tractAdapter.itemCount

    val isInSearchMode: Boolean
        get() = viewModel.listParameters.isInSearchMode

    /** Outlets **/

    private lateinit var tractAdapter: TractListAdapter
    private lateinit var tractLayout: GridLayoutManager

    /***********************************************************************************************
     * View Life Cycle
     **********************************************************************************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.listParameters = it.getSerializable(LIST_PARAM_ARG) as TractListParameters
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tract_recycler, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObservers()
    }

    override fun onAttach(context: Context) {
        callbacks = context as? TractListCallbacks ?: parentFragment as? TractListCallbacks
        super.onAttach(context)
    }

    override fun onDetach() {
        callbacks = null
        super.onDetach()
    }

    /***********************************************************************************************
     * Update
     **********************************************************************************************/

    private fun updateLayout() {
        tractLayout.spanCount = displayMode.spanCount

        tractAdapter.displayMode = displayMode
        tractAdapter.notifyItemRangeChanged(0, tractAdapter.itemCount)
    }

    private fun updateContent() {
        tractAdapter.submitList(viewModel.savedTractWithPictures)
        tractAdapter.notifyDataSetChanged()
        callbacks?.onItemCountChanged(viewModel.savedTractWithPictures.size)
    }
    
    /***********************************************************************************************
     * Observers
     **********************************************************************************************/

    private fun setupObservers() {
        viewModel.tracts.observe(
            owner = viewLifecycleOwner,
            onChanged = { items ->
                items.let {
                    Log.i(TAG, "Got tract items ${it.size}")
                    viewModel.savedTracts = it
                    updateContent()
                }
            })

        viewModel.pictures.observe(
            owner = viewLifecycleOwner,
            onChanged = { items ->
                Log.i(TAG, "Got pictures items ${items.size}")
                viewModel.savedPictures = items
                updateContent()
            })
    }
    
    /***********************************************************************************************
     * Setup
     **********************************************************************************************/

    private fun setupView() {
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        tractLayout = GridLayoutManager(context, displayMode.spanCount)
        tractAdapter = TractListAdapter(callbacks)
        tractAdapter.displayMode = displayMode

        tractAdapter.submitList(viewModel.savedTractWithPictures)
        tractAdapter.notifyDataSetChanged()

        tractRecyclerView.adapter = tractAdapter
        tractRecyclerView.layoutManager = tractLayout
    }

    /***********************************************************************************************
     * Companion
     **********************************************************************************************/

    companion object {

        private const val LIST_PARAM_ARG = "param1"
        
        private val TAG = TractListFragment::class.simpleName ?: "Default"

        /**
         * Use this factory method to create a new instance of
         * this fragment with parameters to display tract list
         *
         * @param listParameters parameters to apply on tract list to display it.
         * @return A new instance of fragment TractRecyclerFragment.
         */
        @JvmStatic
        fun newInstance(listParameters: TractListParameters) =
            TractListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(LIST_PARAM_ARG, listParameters)
                }
            }
    }
}