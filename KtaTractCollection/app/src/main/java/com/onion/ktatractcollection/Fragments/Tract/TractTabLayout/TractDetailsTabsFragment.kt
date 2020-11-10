package com.onion.ktatractcollection.Fragments.Tract.TractTabLayout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.onion.ktatractcollection.R
import kotlinx.android.synthetic.main.fragment_tract_details_tabs.*
import java.util.*

class TractDetailsTabsFragment : Fragment() {

    /**
     * Properties
     */

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
        return inflater.inflate(R.layout.fragment_tract_details_tabs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupTabLayout()
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

    private fun setupTabLayout() {
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