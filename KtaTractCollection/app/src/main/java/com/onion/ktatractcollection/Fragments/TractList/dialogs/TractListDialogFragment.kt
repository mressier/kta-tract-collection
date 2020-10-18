package com.onion.ktatractcollection.Fragments.TractList.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.onion.ktatractcollection.Fragments.TractList.dialogs.TractListParameters.SortOrder
import com.onion.ktatractcollection.R

private const val PARAM_SORT_BY_ID = "sort_by_id"
private const val PARAM_SORT_ORDER_ID = "sort_order_id"

/**
 * A simple [Fragment] subclass.
 * Use the [TractListDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TractListDialogFragment : DialogFragment(), View.OnClickListener {

    interface Callbacks {
        fun onParameterSelected(parameter: TractListParameters)
    }

    /**
     * Properties
     */
    private lateinit var sortByRadioButtons: List<RadioButton>

    private val parametersViewModel: TractListParametersViewModel by lazy {
        ViewModelProvider(this).get(TractListParametersViewModel::class.java)
    }

    /**
     * View Life Cycle
     */

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (savedInstanceState == null) {
            setupParametersFromArguments()
        }

        val dialog = AlertDialog.Builder(requireContext())

        setupChoiceItems(dialog)

        dialog.setTitle(R.string.tract_sort_dialog_title)
        dialog.setPositiveButton(R.string.tract_sort_button) { _, _ -> onComplete() }

        dialog.setCancelable(true)
        dialog.setOnCancelListener { dismiss() }

        return dialog.create()
    }

    /**
     * Setup View
     */

    private fun setupParametersFromArguments() {
        parametersViewModel.sortBy =
            arguments?.getSerializable(PARAM_SORT_BY_ID) as TractListParameters.SortBy
        parametersViewModel.sortOrder =
            arguments?.getSerializable(PARAM_SORT_ORDER_ID) as SortOrder
    }

    private fun setupChoiceItems(dialog: AlertDialog.Builder) {
        val items = TractListParameters.SortBy.values()
        val selectedItem = parametersViewModel.sortBy.ordinal
        val strings = items.map { getString(it.stringId) }.toTypedArray()

        dialog.setSingleChoiceItems(strings, selectedItem) { _, index ->
            onSelectSortOptionItem(index)
        }
    }

    /**
     * Actions
     */

    private fun onSelectSortOptionItem(index: Int) {
        parametersViewModel.sortBy = TractListParameters.SortBy.values()[index]
    }

    private fun onComplete() {
        targetFragment?.let { fragment ->
            (fragment as Callbacks).onParameterSelected(parametersViewModel.parameters)
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
                    putSerializable(PARAM_SORT_BY_ID, parameter.sortOption)
                    putSerializable(PARAM_SORT_ORDER_ID, parameter.sortOrder)
                }
            }
    }

    override fun onClick(v: View?) {
        println("click click")
    }
}