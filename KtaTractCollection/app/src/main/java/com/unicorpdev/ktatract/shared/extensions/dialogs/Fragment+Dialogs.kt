package com.unicorpdev.ktatract.shared.extensions.dialogs

import android.app.AlertDialog
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.shared.views.MaterialTextButton
import kotlinx.android.synthetic.main.dialog_loading_view.view.*
import kotlinx.android.synthetic.main.dialog_text_view.view.*
import java.util.*

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
        pictureTextView.text = text
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
 * Action Dialog
 */

data class DialogAction(
    @StringRes val title: Int,
    @ColorRes val color: Int? = null,
    val callback: (() -> Unit)? = null
) {}

fun Fragment.showActionDialog(@StringRes title: Int? = null,
                              actions: Array<DialogAction>): AlertDialog {
    val builder = AlertDialog.Builder(requireContext())
    val customView = layoutInflater.inflate(R.layout.dialog_action_list, null)

    title?.let { builder.setTitle(it) }
    builder.setView(customView)
    builder.setNegativeButton(getString(android.R.string.cancel)) { dialog, _ -> dialog.dismiss() }

    val alertDialog = builder.create()

    val linearLayout = customView as? LinearLayoutCompat
    for (action in actions) {
        val button = MaterialTextButton(requireContext()).apply {
            text = getString(action.title)
            action.color?.let { setTextColor(resources.getColor(it)) }
            setOnClickListener {
                action.callback?.invoke()
                alertDialog.dismiss()
            }
        }

        linearLayout?.addView(button)
    }

    alertDialog.show()
    return alertDialog
}
