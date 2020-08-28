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

class TractDialogFragment: DialogFragment() {

    interface Callbacks {
        fun onDelete(tractId: UUID)
    }

    enum class ArgumentsKey {
        TRACT_ID
    }

    /**
     * Properties
     */

    private lateinit var callbacks: Callbacks
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
        val view = inflater.inflate(R.layout.tract_dialog_fragment, container)
        setupView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val tractString = arguments?.getSerializable(ArgumentsKey.TRACT_ID.name) as String
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
            args.putSerializable(ArgumentsKey.TRACT_ID.name, tractId.toString())

            val fragment = TractDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}