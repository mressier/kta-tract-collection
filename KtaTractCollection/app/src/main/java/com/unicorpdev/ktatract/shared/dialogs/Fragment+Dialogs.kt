package com.unicorpdev.ktatract.shared.dialogs

import android.app.AlertDialog
import androidx.fragment.app.Fragment
import com.unicorpdev.ktatract.R
import kotlinx.android.synthetic.main.dialog_loading_view.view.*
import kotlinx.android.synthetic.main.dialog_text_view.view.*
import kotlinx.android.synthetic.main.dialog_tract_action.view.*
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

    val customView = layoutInflater.inflate(R.layout.dialog_loading_view, null)
    customView.progressBar.isIndeterminate = true

    builder.setTitle(R.string.loading_in_progress)
    builder.setView(customView)
    builder.setCancelable(false)

    val alert = builder.create()
    alert.show()
    return alert
}

fun Fragment.showErrorDialog(title: String, text: String): AlertDialog {
    val builder = AlertDialog.Builder(requireContext())
    val textView = layoutInflater.inflate(R.layout.dialog_text_view, null).apply {
        textView.text = text
    }

    builder.setTitle(title)
    builder.setView(textView)
    builder.setPositiveButton(R.string.ok) { dialogInterface, _ ->
        dialogInterface.dismiss()
    }

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

    val customView = layoutInflater.inflate(R.layout.dialog_tract_action, null)
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