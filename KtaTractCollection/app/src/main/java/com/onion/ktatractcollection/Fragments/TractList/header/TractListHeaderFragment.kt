package com.onion.ktatractcollection.Fragments.TractList.header

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.onion.ktatractcollection.Fragments.TractList.dialogs.TractListParameters
import com.onion.ktatractcollection.R
import kotlinx.android.synthetic.main.fragment_tract_list_header.*

class TractListHeaderFragment : Fragment() {

    interface Callbacks {
        fun onDisplayModeChanged(displayMode: TractListParameters.DisplayMode)
    }

    /**
     * Properties
     */

    private val viewModel: TractListHeaderViewModel by lazy {
        ViewModelProvider(this).get(TractListHeaderViewModel::class.java)
    }

    private var callbacks: Callbacks? = null

    /**
     * View Life Cycle
     */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tract_list_header, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModelObserver()
        setupTractListButtonListener()

        updateHeaderView(viewModel.displayMode)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = parentFragment as? Callbacks
    }

    override fun onDetach() {
        callbacks = null
        super.onDetach()
    }

    /**
     * Update
     */

    private fun updateHeaderView(displayMode: TractListParameters.DisplayMode) {
        tractListButton.setImageResource(displayMode.iconId)
        tractListButton.contentDescription = getString(displayMode.titleId)
    }

    private fun updateTractNumber(count: Int) {
        tractCountTextView.text = getString(R.string.tract_count).format(count)
    }

    /**
     * Setup
     */
    private fun setupViewModelObserver() {
        viewModel.tracts.observe(
            owner = viewLifecycleOwner,
            onChanged = { items ->
                updateTractNumber(items.size)
            }
        )
    }

    private fun setupTractListButtonListener() {
        tractListButton.setOnClickListener {
            viewModel.displayMode = viewModel.displayMode.reversed

            updateHeaderView(viewModel.displayMode)
            callbacks?.onDisplayModeChanged(viewModel.displayMode)
        }
    }

    /**
     * Companion
     */
    companion object {
        @JvmStatic
        fun newInstance() = TractListHeaderFragment()
    }
}