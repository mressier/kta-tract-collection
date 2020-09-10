package com.onion.ktatractcollection.Fragments.TractList

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.onion.ktatractcollection.R

private const val SORT_OPTION_ID = "sort_option_id"

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
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioButtons: List<RadioButton>
    private lateinit var validateButton: Button

    private lateinit var parameter: TractListParameters

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

        setupListeners()
        updateUI()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val sort =
            arguments?.getSerializable(SORT_OPTION_ID) as TractListParameters.Sort

        parameter = TractListParameters().apply { sortOption = sort }
        return super.onCreateDialog(savedInstanceState)
    }

    /**
     * Setup
     */

    private fun setupView(view: View) {
        radioGroup = view.findViewById(R.id.radio_group)
        validateButton = view.findViewById(R.id.validate_button)
        setupRadioButtons(radioGroup)
    }

    private fun setupRadioButtons(radioGroup: RadioGroup) {
        val buttonsValue = TractListParameters.Sort.values()
        radioButtons = buttonsValue.map { value ->
            val button =
                RadioButton(requireContext()).apply { setText(value.stringId) }
            radioGroup.addView(button)
            button
        }
    }

    private fun setupListeners() {
        setupRadioGroupListener()
        setupValidateButtonListener()
    }

    private fun setupRadioGroupListener() {
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val button: RadioButton = group.findViewById(checkedId)
            val index = radioButtons.indexOf(button)
            val selected = TractListParameters.Sort.values().get(index)
            parameter.sortOption = selected
        }
    }

    private fun setupValidateButtonListener() {
        validateButton.setOnClickListener {
            targetFragment?.let { fragment ->
                (fragment as TractListDialogFragment.Callbacks).onParameterSelected(parameter)
                dismiss()
            }
        }
    }

    /**
     * Update
     */
    private fun updateUI() {
        val buttonId = radioButtons.get(parameter.sortOption.ordinal).id
        if (buttonId != radioGroup.checkedRadioButtonId) {
            radioGroup.check(buttonId)
        }
    }

    /**
     * Initialize
     */

    companion object {
        @JvmStatic
        fun newInstance(parameter: TractListParameters) =
            TractListDialogFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(SORT_OPTION_ID, parameter.sortOption)
                }
            }
    }
}