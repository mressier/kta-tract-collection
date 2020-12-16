package com.unicorpdev.ktatract.shared.extensions.dialogs

import android.app.AlertDialog
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.unicorpdev.ktatract.fragments.tractList.parameters.TractListParameters
import com.unicorpdev.ktatract.R
import kotlinx.android.synthetic.main.dialog_sort_tract_list.view.*

/**
 * Sort List Dialog
 */

interface SortListCallbacks {
    fun onParameterSelected(parameter: TractListParameters)
}

fun Fragment.showSortListDialog(
    parameters: TractListParameters,
    callbacks: SortListCallbacks
): AlertDialog {
    val builder = AlertDialog.Builder(requireContext())
    val customView = inflateTractListSortView(parameters)

    builder.setView(customView)
    builder.setTitle(R.string.tract_sort_dialog_title)
    builder.setPositiveButton(R.string.tract_sort_button) { dialogInterface, _ ->
        callbacks.onParameterSelected(updateParameters(parameters, customView))
        dialogInterface.dismiss()
    }

    builder.setNeutralButton(android.R.string.cancel) { dialogInterface, _ ->
        dialogInterface.dismiss()
    }

    builder.setOnCancelListener {
        callbacks.onParameterSelected(updateParameters(parameters, customView))
    }

    builder.setCancelable(true)

    val alertDialog = builder.create()
    alertDialog.show()
    return alertDialog
}

private fun updateParameters(parameters: TractListParameters, view: View): TractListParameters {
    val sortBy = TractListParameters.SortOption.values()[view.sortByRadioGroup.checkedRadioButtonId]

    val sortOrder = if (view.descendingCheckBox.isChecked) {
        TractListParameters.SortOrder.DESCENDING
    } else {
        TractListParameters.SortOrder.ASCENDING
    }

    val showFavorites = view.favoriteCheckBox.isChecked

    return parameters.apply {
        this.sortOption = sortBy
        this.sortOrder = sortOrder
        this.showOnlyFavorites = showFavorites
    }
}

private fun Fragment.inflateTractListSortView(
    parameters: TractListParameters,
): View {
    val customView = layoutInflater.inflate(R.layout.dialog_sort_tract_list, null)
    val buttonValues = TractListParameters.SortOption.values()
    val radioButtons = buttonValues.map { requireContext().getString(it.stringId) }.toTypedArray()

    setupSortListRadioButtons(customView.sortByRadioGroup, radioButtons)
    setupSortListView(customView, parameters)

    return customView
}

private fun Fragment.setupSortListRadioButtons(radioGroup: RadioGroup, values: Array<String>) {
    values.mapIndexed { index, value ->
        val button = RadioButton(requireContext()).apply {
            text = value
            id = index
        }
        radioGroup.addView(button)
    }
}

private fun setupSortListView(view: View, parameters: TractListParameters) {
    val radioSelectedItem = parameters.sortOption.ordinal
    val isDescendingSort = parameters.sortOrder == TractListParameters.SortOrder.DESCENDING

    view.sortByRadioGroup.check(radioSelectedItem)
    view.descendingCheckBox.isChecked = isDescendingSort
    view.favoriteCheckBox.isChecked = parameters.showOnlyFavorites
}
