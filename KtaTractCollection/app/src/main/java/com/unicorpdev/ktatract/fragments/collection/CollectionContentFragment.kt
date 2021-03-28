package com.unicorpdev.ktatract.fragments.collection

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.fragments.tractList.AllTractsFragment
import com.unicorpdev.ktatract.fragments.tractList.list.TractListFragment
import com.unicorpdev.ktatract.models.TractCollection
import kotlinx.android.synthetic.main.fragment_about_page.*
import kotlinx.android.synthetic.main.fragment_collection_content.*
import java.util.*

class CollectionContentFragment : Fragment() {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private val viewModel by viewModels<CollectionContentViewModel>()

    /** Outlets **/
    private lateinit var headerFragment: CollectionHeaderFragment
    private lateinit var listFragment: AllTractsFragment

    /***********************************************************************************************
     * View Life Cycle
     **********************************************************************************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val args = CollectionContentFragmentArgs.fromBundle(it)
            args.collectionId?.let { collectionId ->
                val id = UUID.fromString(collectionId)
                viewModel.collectionId = id
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collection_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragments()
        loadCollection(viewModel.collectionId)
    }

    /***********************************************************************************************
     * Setup
     **********************************************************************************************/

    private fun loadCollection(collectionId: UUID?) {
        headerFragment.loadCollection(collectionId)
        listFragment.loadCollection(collectionId)
    }

    private fun setupFragments() {
        headerFragment =
            childFragmentManager.findFragmentById(R.id.headerFragment) as CollectionHeaderFragment
        listFragment =
            childFragmentManager.findFragmentById(R.id.listFragment) as AllTractsFragment
        listFragment.onSearch = { text ->
            val scrollPosition = collectionContentScrollView.verticalScrollbarPosition
            if (scrollPosition == 0) {
                val height = headerFragment.view?.height ?: 0
                collectionContentScrollView.smoothScrollTo(0, height)
            }
        }
    }

    /***********************************************************************************************
     * Companion
     **********************************************************************************************/

    companion object {

        private val TAG = CollectionContentFragment::class.simpleName ?: "Default"

        @JvmStatic
        fun newInstance() = CollectionContentFragment()
    }
}