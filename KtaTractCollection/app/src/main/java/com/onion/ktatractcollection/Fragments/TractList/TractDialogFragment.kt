package com.onion.ktatractcollection.Fragments.TractList

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.onion.ktatractcollection.R
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

    private lateinit var deleteButton: Button
    private lateinit var cancelButton: Button

    /**
     * View Life Cycle
     */

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tract_dialog, container)
        setupView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val tractString = arguments?.getSerializable(ARGUMENT_TRACT_ID) as String
        tractId = UUID.fromString(tractString)

        return super.onCreateDialog(savedInstanceState)
    }

    /**
     * Setup
     */
    private fun setupView(view: View) {
        deleteButton = view.findViewById(R.id.delete_button)
        cancelButton = view.findViewById(R.id.cancel_button)
    }

    private fun setupListeners() {
        deleteButton.setOnClickListener {
            targetFragment?.let { fragment ->
                (fragment as TractDialogFragment.Callbacks).onDelete(tractId)
                dismiss()
            }
        }

        cancelButton.setOnClickListener {
            dismiss()
        }
    }
    /**
     * Instantiate
     */
    companion object {
        fun newInstance(tractId: UUID): TractDialogFragment {
            val args = Bundle()
            args.putSerializable(ARGUMENT_TRACT_ID, tractId.toString())

            val fragment = TractDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}