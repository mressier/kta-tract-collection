package com.unicorpdev.ktatract.fragments.collectionList

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.fragments.collectionList.list.TractCollectionCallback
import com.unicorpdev.ktatract.fragments.collectionList.list.CollectionListFragment
import com.unicorpdev.ktatract.shared.fragments.AddButtonFragment
import com.unicorpdev.ktatract.shared.fragments.listHeader.ListHeaderFragment
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [AllCollectionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllCollectionsFragment : Fragment(),
    CollectionListFragment.Callbacks,
    AddButtonFragment.Callbacks
{

    interface Callbacks: TractCollectionCallback {
    }

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private lateinit var headerFragment: ListHeaderFragment

    private var callbacks: Callbacks? = null

    private val viewModel by viewModels<AllCollectionsViewModel>()

    /***********************************************************************************************
     * View Life Cycle
     **********************************************************************************************/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_collections, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupChildFragments()
        setupHeaderFragment()
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

    private fun setupChildFragments() {
        headerFragment =
            childFragmentManager.findFragmentById(R.id.headerFragment) as ListHeaderFragment
    }

    private fun setupHeaderFragment() {
        headerFragment.setCounterFormatString(R.string.collection_count)
        headerFragment.shouldShowDisplayModeButton(false)
    }
    
    /***********************************************************************************************
     * Tract Collection Callback
     **********************************************************************************************/

    override fun onCollectionSelected(collectionId: UUID) {
        callbacks?.onCollectionSelected(collectionId)
    }

    override fun onItemCountChanged(itemCount: Int) {
        headerFragment.updateItemCount(itemCount)
    }

    override fun onAddButtonSelected() {
        val collectionId = viewModel.createCollection()
        callbacks?.onCollectionSelected(collectionId)
    }

    /***********************************************************************************************
     * Companion
     **********************************************************************************************/

    companion object {
        @JvmStatic
        fun newInstance() = AllCollectionsFragment()
    }

}