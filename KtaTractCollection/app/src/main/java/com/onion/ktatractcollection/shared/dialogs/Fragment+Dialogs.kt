package com.onion.ktatractcollection.shared.dialogs

import android.app.AlertDialog
import androidx.fragment.app.Fragment
import com.onion.ktatractcollection.Fragments.TractList.dialogs.TractListParameters
import com.onion.ktatractcollection.R
import kotlinx.android.synthetic.main.fragment_tract_dialog.view.*
import java.util.*

/**
 * Sort List Dialog
 */

interface SortListCallbacks {
    fun onParameterSelected(parameter: TractListParameters)
}

fun Fragment.showSortListDialog(parameters: TractListParameters, callbacks: SortListCallbacks): AlertDialog {
    val builder = AlertDialog.Builder(requireContext())

    setupChoiceItems(builder, parameters, callbacks)

    builder.setTitle(R.string.tract_sort_dialog_title)
    builder.setPositiveButton(R.string.tract_sort_button) { dialogInterface, _ ->
        dialogInterface.dismiss()
    }

    builder.setCancelable(true)

    val alertDialog = builder.create()
    alertDialog.show()
    return alertDialog
}

private fun Fragment.setupChoiceItems(
    builder: AlertDialog.Builder,
    parameters: TractListParameters,
    callbacks: SortListCallbacks
) {
    val items = TractListParameters.SortBy.values()
    val selectedItem = parameters.sortOption.ordinal
    val strings = items.map { getString(it.stringId) }.toTypedArray()

    builder.setSingleChoiceItems(strings, selectedItem) { _, index ->
        val newParameters = TractListParameters().apply {
            sortOption = TractListParameters.SortBy.values()[index]
        }
        callbacks.onParameterSelected(newParameters)
    }
}

/**
 * Tract Action Dialog
 */

interface TractActionCallback {
    fun onDelete(tractId: UUID)
}

fun Fragment.showTractActionDialog(tractId: UUID, callbacks: TractActionCallback): AlertDialog {
    val builder = AlertDialog.Builder(requireContext())

    val customView = layoutInflater.inflate(R.layout.fragment_tract_dialog, null)
    builder.setView(customView)
    builder.setNegativeButton(getString(android.R.string.cancel)) { dialog, _ -> dialog.dismiss() }

    val alertDialog = builder.create()

    customView.deleteButton.setOnClickListener {
        callbacks.onDelete(tractId)
        alertDialog.dismiss()
    }

    alertDialog.show()
    return alertDialog
}