package com.onion.ktatractcollection.Fragments.TractList.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.onion.ktatractcollection.Fragments.TractList.TractListParameters
import com.onion.ktatractcollection.R

private const val PARAM_SORT_BY_ID = "sort_by_id"
private const val PARAM_SORT_ORDER_ID = "sort_order_id"

/**
 * A simple [Fragment] subclass.
 * Use the [TractListDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TractListDialogFragment : DialogFragment() {

    interface Callbacks {
        fun onParameterSelected(parameter: TractListParameters)
    }

    /**
     * Properties
     */
    private lateinit var sortByRadioGroup: RadioGroup
    private lateinit var sortByRadioButtons: List<RadioButton>
    private lateinit var sortOrderRadioGroup: RadioGroup
    private lateinit var sortOrderRadioButtons: List<RadioButton>
    private lateinit var validateButton: Button

    private val parametersViewModel: TractListParametersViewModel by lazy {
        ViewModelProvider(this).get(TractListParametersViewModel::class.java)
    }

    /**
     * View Life Cycle
     */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tract_list_dialog, container, false)
        setupView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        println("onViewCreated()")
        setupListeners()
        updateUI()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (savedInstanceState == null) {
            parametersViewModel.sortBy =
                arguments?.getSerializable(PARAM_SORT_BY_ID) as TractListParameters.SortBy
            parametersViewModel.sortOrder =
                arguments?.getSerializable(PARAM_SORT_ORDER_ID) as TractListParameters.SortOrder
        }

        return super.onCreateDialog(savedInstanceState)
    }

    /**
     * Setup View
     */

    private fun setupView(view: View) {
        sortByRadioGroup = view.findViewById(R.id.sort_by_radio_group)
        sortOrderRadioGroup = view.findViewById(R.id.sort_order_radio_group)
        validateButton = view.findViewById(R.id.validate_button)

        sortByRadioButtons = setupRadioButtons(
            sortByRadioGroup,
            TractListParameters.SortBy.values().map { it.stringId }
        )
        sortOrderRadioButtons = setupRadioButtons(
            sortOrderRadioGroup,
            TractListParameters.SortOrder.values().map { it.stringId }
        )
    }

    private fun setupRadioButtons(
        radioGroup: RadioGroup,
        valuesStringId: List<Int>
    ): List<RadioButton> {
        return valuesStringId.map { value ->
            val button =
                RadioButton(requireContext()).apply { setText(value) }
            radioGroup.addView(button)
            button
        }
    }

    /**
     * Setup Listeners
     */
    private fun setupListeners() {
        setupSortByRadioGroupListener()
        setupSortOrderRadioGroupListener()
        setupValidateButtonListener()
    }

    private fun setupSortByRadioGroupListener() {
        sortByRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            val index = getCheckedRadioButtonIndex(group, sortByRadioButtons, checkedId)
            val selected = TractListParameters.SortBy.values()[index]

            parametersViewModel.sortBy = selected
        }
    }

    private fun setupSortOrderRadioGroupListener() {
        sortOrderRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            val index = getCheckedRadioButtonIndex(group, sortOrderRadioButtons, checkedId)
            val selected = TractListParameters.SortOrder.values()[index]

            parametersViewModel.sortOrder = selected
        }
    }

    private fun setupValidateButtonListener() {
        validateButton.setOnClickListener {
            onParametersSelected(parametersViewModel.parameters)
            dismiss()
        }
    }

    private fun onParametersSelected(parameters: TractListParameters) {
        targetFragment?.let { fragment ->
            (fragment as Callbacks).onParameterSelected(parameters)
        }
    }

    /**
     * Update
     */
    private fun updateUI() {
        updateRadioButtonChecked(
            sortByRadioGroup,
            sortByRadioButtons,
            parametersViewModel.sortBy.ordinal
        )
        updateRadioButtonChecked(
            sortOrderRadioGroup,
            sortOrderRadioButtons,
            parametersViewModel.sortOrder.ordinal
        )
    }

    private fun updateRadioButtonChecked(radioGroup: RadioGroup, radioButtons: List<RadioButton>, checkIndex: Int) {
        val buttonId = radioButtons[checkIndex].id
        if (buttonId != radioGroup.checkedRadioButtonId) {
            radioGroup.check(buttonId)
        }
    }

    /**
     * Tools
     */
    private fun getCheckedRadioButtonIndex(
        radioGroup: RadioGroup,
        radioButtons: List<RadioButton>,
        checkedId: Int
    ): Int {
        val button: RadioButton = radioGroup.findViewById(checkedId)
        return radioButtons.indexOf(button)
    }

    /**
     * Initialize
     */

    companion object {
        @JvmStatic
        fun newInstance(parameter: TractListParameters) =
            TractListDialogFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(PARAM_SORT_BY_ID, parameter.sortOption)
                    putSerializable(PARAM_SORT_ORDER_ID, parameter.sortOrder)
                }
            }
    }
}