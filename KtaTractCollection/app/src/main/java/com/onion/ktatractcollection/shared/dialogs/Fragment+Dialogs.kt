package com.onion.ktatractcollection.shared.dialogs

import android.app.AlertDialog
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.onion.ktatractcollection.Fragments.TractList.dialogs.TractListParameters
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.R
import kotlinx.android.synthetic.main.fragment_tract_dialog.view.*
import kotlinx.android.synthetic.main.fragment_tract_list_sort_dialog.view.*
import java.util.*

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