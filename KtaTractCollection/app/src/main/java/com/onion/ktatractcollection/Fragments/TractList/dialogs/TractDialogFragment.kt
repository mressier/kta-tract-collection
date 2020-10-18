package com.onion.ktatractcollection.Fragments.TractList.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.onion.ktatractcollection.R
import kotlinx.android.synthetic.main.fragment_tract_dialog.*
import java.util.*

private const val ARGUMENT_TRACT_ID = "tract_id"

class TractDialogFragment: DialogFragment() {

    interface Callbacks {
        fun onDelete(tractId: UUID)
    }

    /**
     * Properties
     */

    private lateinit var tractId: UUID

    /**
     * View Life Cycle
     */

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val tractString = arguments?.getSerializable(ARGUMENT_TRACT_ID) as String
        tractId = UUID.fromString(tractString)

        val dialog = AlertDialog.Builder(requireContext())

        val view = layoutInflater.inflate(R.layout.fragment_tract_dialog, null)
        setupListeners(view)
        dialog.setView(view)

        dialog.setNegativeButton(getString(android.R.string.cancel)) { _, _ ->
            dismiss()
        }

        return dialog.create()
    }

    /**
     * Setup
     */

    private fun setupListeners(view: View) {
        val deleteButton: Button = view.findViewById(R.id.deleteButton)

        deleteButton.setOnClickListener {
            targetFragment?.let { fragment ->
                (fragment as Callbacks).onDelete(tractId)
                dismiss()
            }
        }
    }

    /**
     * Instantiate
     */
    companion object {
        fun newInstance(tractId: UUID): TractDialogFragment {
            val args = Bundle().apply {
                putSerializable(ARGUMENT_TRACT_ID, tractId.toString())
            }
            return TractDialogFragment().apply {
                arguments = args
            }
        }
    }
}