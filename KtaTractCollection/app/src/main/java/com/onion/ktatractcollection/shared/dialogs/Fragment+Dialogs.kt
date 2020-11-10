package com.onion.ktatractcollection.shared.dialogs

import android.app.AlertDialog
import androidx.fragment.app.Fragment
import com.onion.ktatractcollection.R
import kotlinx.android.synthetic.main.fragment_loading_dialog.view.*
import kotlinx.android.synthetic.main.fragment_tract_dialog.view.*
import java.util.*

/**
 * Tract Action Dialog
 */

interface TractActionCallback {
    fun onDelete(tractId: UUID)
}
/**
 * Show a dialog with a circle loading bar
 *
 * @return the created alert dialog
 */
fun Fragment.showLoadingDialog(): AlertDialog {
    val builder = AlertDialog.Builder(requireContext())

    val customView = layoutInflater.inflate(R.layout.fragment_loading_dialog, null)
    customView.progressBar.isIndeterminate = true

    builder.setTitle(R.string.loading_in_progress)
    builder.setView(customView)
    builder.setCancelable(false)

    val alert = builder.create()
    alert.show()
    return alert
}

/**
 * Show different actions that can be performed on a given tract
 *
 * @property tractId The tract on which perform the action
 * @property callbacks callbacks for each action
 * @return .
 */
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