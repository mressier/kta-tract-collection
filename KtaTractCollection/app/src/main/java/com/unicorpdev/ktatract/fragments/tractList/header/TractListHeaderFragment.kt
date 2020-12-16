package com.unicorpdev.ktatract.fragments.tractList.header

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.unicorpdev.ktatract.fragments.tractList.parameters.DisplayMode
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.shared.analytics.KtaTractAnalytics
import com.unicorpdev.ktatract.shared.analytics.KtaTractAnalytics.SelectEvent
import kotlinx.android.synthetic.main.fragment_tract_list_header.*

class TractListHeaderFragment : Fragment() {

    interface Callbacks {
        fun onDisplayModeChanged(displayMode: DisplayMode)
    }

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private val viewModel: TractListHeaderViewModel by lazy {
        ViewModelProvider(this).get(TractListHeaderViewModel::class.java)
    }

    private var callbacks: Callbacks? = null

    @StringRes
    private var counterFormatString: Int = R.string.list_default_count
    private var showDisplayModeButton: Boolean = true

    /***********************************************************************************************
     * View Life Cycle
     **********************************************************************************************/

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
        updateHeaderViewButton()
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

    /**
     * Change the item count displayed on the header
     *
     * @property count number to display
     */
    fun updateItemCount(count: Int) {
        val description = getString(counterFormatString)
        tractCountTextView.text = getString(R.string.list_format).format(count, description)
    }

    /**
     * Show or not the button that can toggle list format.
     * When pressed, the button call the callback `onDisplayModeChanged`.
     * By default the button is displayed.
     *
     * @property shouldShow show or not the list/grid button
     */
    fun shouldShowDisplayModeButton(shouldShow: Boolean) {
        showDisplayModeButton = shouldShow
        if (view != null) {
            updateHeaderViewButton()
        }
    }

    /**
     * Change the string description of the number that is displayed
     *
     * @property string resId of the string to use as counter description. Should be on the form
     */
    fun setCounterFormatString(@StringRes string: Int) {
        counterFormatString = string
    }
    
    /***********************************************************************************************
     * Tools
     **********************************************************************************************/
    
    private fun updateHeaderView() {
        val displayMode = viewModel.displayMode.reversed
        tractListButton.setImageResource(displayMode.iconId)
        tractListButton.contentDescription = getString(displayMode.titleId)
    }

    private fun updateHeaderViewButton() {
        tractListButton.visibility =
            if (showDisplayModeButton) { View.VISIBLE } else { View.GONE }
    }

    /***********************************************************************************************
     * Setup
     **********************************************************************************************/

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