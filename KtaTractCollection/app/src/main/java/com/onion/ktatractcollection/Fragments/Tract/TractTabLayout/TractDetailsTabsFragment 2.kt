package com.onion.ktatractcollection.Fragments.Tract.TractTabLayout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.onion.ktatractcollection.R
import java.util.*

class TractDetailsTabsFragment : Fragment() {

    /**
     * Properties
     */

    /* Outlets */
    private lateinit var tractViewPager: ViewPager2
    private lateinit var tractTabLayout: TabLayout

    private lateinit var adapter : TractDetailsTabsAdapter

    private val tabs = arrayOf("IMAGES") // Next feature: Map

    /* View Model */
    private val viewModel: TractDetailsTabsViewModel by lazy {
        ViewModelProvider(this).get(TractDetailsTabsViewModel::class.java)
    }

    /**
     * View Life cycle
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tract_details_tabs, container, false)
        setupView(view)
        return view
    }

    /**
     * Parameters
     */

    fun setTract(tractId: UUID) {
        viewModel.tractId = tractId
    }

    /**
     * Setup
     */
    private fun setupView(view: View) {
        tractViewPager = view.findViewById(R.id.tract_view_pager)
        tractTabLayout = view.findViewById(R.id.tract_tab_layout)

        setupAdapter()

        TabLayoutMediator(tractTabLayout, tractViewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }

    private fun setupAdapter() {
        adapter = TractDetailsTabsAdapter(this, tabs.count(), viewModel.tractId)
        tractViewPager.adapter = adapter
    }

    /**
     * Companion
     */
    companion object {
        private const val TAG = "TractDetailsTabs"
    }
}