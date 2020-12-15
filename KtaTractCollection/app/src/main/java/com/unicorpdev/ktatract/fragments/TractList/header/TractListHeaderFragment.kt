package com.unicorpdev.ktatract.fragments.TractList.header

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.unicorpdev.ktatract.fragments.TractList.parameters.DisplayMode
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.shared.analytics.KtaTractAnalytics
import com.unicorpdev.ktatract.shared.analytics.KtaTractAnalytics.SelectEvent
import kotlinx.android.synthetic.main.fragment_tract_list_header.*

class TractListHeaderFragment : Fragment() {

    interface Callbacks {
        fun onDisplayModeChanged(displayMode: DisplayMode)
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
        setupTractListButtonListener()

        updateHeaderView()
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

    private fun updateHeaderView() {
        val displayMode = viewModel.displayMode.reversed
        tractListButton.setImageResource(displayMode.iconId)
        tractListButton.contentDescription = getString(displayMode.titleId)
    }

    fun updateTractNumber(count: Int) {
        tractCountTextView.text = getString(R.string.tract_count).format(count)
    }

    /**
     * Setup
     */

    private fun setupTractListButtonListener() {
        tractListButton.setOnClickListener {
            KtaTractAnalytics.logSelectItem(SelectEvent.LIST_DISPLAY)

            viewModel.displayMode = viewModel.displayMode.reversed
            updateHeaderView()
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