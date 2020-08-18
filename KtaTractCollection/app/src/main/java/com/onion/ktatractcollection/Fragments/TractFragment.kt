package com.onion.ktatractcollection.Fragments

import android.app.DatePickerDialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.R
import com.onion.ktatractcollection.Shared.Fragments.DatePickerFragment
import com.onion.ktatractcollection.Shared.Tools.TextChangedWatcher
import java.text.DateFormat
import java.util.*

class TractFragment : Fragment(), DatePickerFragment.Callbacks {

    private enum class Requests {
        DATE
    }

    private enum class Dialogs(val description: String) {
        DATE("dateDialog")
    }

    companion object {
        fun newInstance() = TractFragment()
    }

    /**
     * Properties
     */

    private lateinit var authorTextField: EditText
    private lateinit var dateButton: Button
    private lateinit var commentsTextField: EditText

    /* View Model */
    private lateinit var viewModel: TractViewModel
    private var tract = Tract()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.tract_fragment, container, false)
        setupOutlets(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TractViewModel::class.java)
        tract = viewModel.tract
        updateView(tract)
    }

    override fun onStart() {
        setupListeners()
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        viewModel.tract = tract
    }

    /**
     * Update
     */

    private fun updateView(tract: Tract) {
        authorTextField.setText(tract.author)
        dateButton.text = DateFormat.getDateInstance(DateFormat.LONG).format(tract.discoveryDate)
        commentsTextField.setText(tract.comment)
    }

    /**
     * Setup
     */

    private fun setupListeners() {
        setupAuthorListener()
        setupCommentsListener()
        setupDateListener()
    }

    private fun setupDateListener() {
        dateButton.setOnClickListener {
            DatePickerFragment.newInstance(tract.discoveryDate, Date()).apply {
                setTargetFragment(this@TractFragment, Requests.DATE.ordinal)
                show(this@TractFragment.requireFragmentManager(), Dialogs.DATE.description)
            }
        }
    }

    private fun setupAuthorListener() {
        val authorWatcher = TextChangedWatcher() { tract.author = it }
        authorTextField.addTextChangedListener(authorWatcher)
    }
    private fun setupCommentsListener() {
        val commentsWatcher = TextChangedWatcher() { tract.comment = it }
        commentsTextField.addTextChangedListener(commentsWatcher)
    }

    private fun setupOutlets(view: View) {
        authorTextField = view.findViewById(R.id.author_text_field)
        dateButton = view.findViewById(R.id.date_button)
        commentsTextField = view.findViewById(R.id.comments_text_field)
    }

    override fun onDateSelected(date: Date) {
        tract.discoveryDate = date
        updateView(tract)
    }

}
