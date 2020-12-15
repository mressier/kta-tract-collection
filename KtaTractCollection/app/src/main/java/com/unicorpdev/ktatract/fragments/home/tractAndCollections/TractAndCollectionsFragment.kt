package com.unicorpdev.ktatract.fragments.home.tractAndCollections

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.unicorpdev.ktatract.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_tract_and_collections.*

/**
 * A simple [Fragment] subclass.
 * Use the [TractAndCollectionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TractAndCollectionsFragment : Fragment() {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private lateinit var adapter : TractAndCollectionsTabsAdapter

    private val tabs = TractAndCollectionsTabsAdapter.TabContent.values()

    /***********************************************************************************************
     * View Life Cycle
     **********************************************************************************************/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tract_and_collections, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupTabLayout()
    }

    /***********************************************************************************************
     * Setup
     **********************************************************************************************/

    private fun setupTabLayout() {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getString(tabs[position].title)
        }.attach()
    }

    private fun setupAdapter() {
        adapter = TractAndCollectionsTabsAdapter(this, tabs)
        viewPager.adapter = adapter
    }

    /***********************************************************************************************
     * Companion
     **********************************************************************************************/

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         */

        @JvmStatic
        fun newInstance() = TractAndCollectionsFragment()
    }
}