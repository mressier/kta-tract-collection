package com.unicorpdev.ktatract.fragments.collectionList.spinner

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.unicorpdev.ktatract.R
import kotlinx.android.synthetic.main.fragment_collection_spinner.*
import kotlinx.coroutines.selects.select
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [CollectionSpinnerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CollectionSpinnerFragment: Fragment(), AdapterView.OnItemSelectedListener {

    interface Callbacks {
        fun onCollectionSelected(collectionId: UUID)
    }

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    var selectedCollectionId: UUID
        get() = viewModel.selectedCollectionId
        set(value) {
            if (view != null) {
                viewModel.selectedCollectionId = value
                updateUI()
            }
        }

    /** Private **/

    private var callbacks: Callbacks? = null

    private val viewModel by viewModels<CollectionSpinnerViewModel>()

    /** Outlets **/

    private val spinnerAdapter: ArrayAdapter<String> by lazy {
        ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            viewModel.collectionsNames
        )
    }

    /***********************************************************************************************
     * View Life Cycle
     **********************************************************************************************/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_collection_spinner, container, false)
        setupViewModel()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupListeners()
        setupSpinner()
        updateUI()
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
     * Methods
     **********************************************************************************************/

    private fun updateUI() {
        println("UPDATE UI - collection : ${viewModel.selectedCollectionId} - position ${viewModel.getPositionForCurrentCollection()} ")
        spinner.setSelection(viewModel.getPositionForCurrentCollection())
    }

    /***********************************************************************************************
     * Setup
     **********************************************************************************************/

    private fun setupObservers() {
        viewModel.collections.observe(viewLifecycleOwner) { collections ->
            spinnerAdapter.clear()
            viewModel.savedCollections = collections
            viewModel.collectionsNames.map {
                spinnerAdapter.add(it)
            }
            updateUI()
        }
    }
    
    private fun setupListeners() {
        spinner.onItemSelectedListener = this
    }

    private fun setupSpinner() {
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
    }

    private fun setupViewModel() {
        viewModel.context = requireContext()
    }

    /***********************************************************************************************
     * Callbacks
     **********************************************************************************************/
    
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val collection = viewModel.getCollectionIdAtIndex(position)
        callbacks?.onCollectionSelected(collection)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        println("nothing selected")
    }

    /***********************************************************************************************
     * Companion
     **********************************************************************************************/
    
    companion object {
        
        @JvmStatic
        fun newInstance() = CollectionSpinnerFragment()
    }

}