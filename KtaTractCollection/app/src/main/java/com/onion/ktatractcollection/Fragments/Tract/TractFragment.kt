package com.onion.ktatractcollection.Fragments.Tract

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.R
import com.onion.ktatractcollection.shared.Fragments.DatePickerFragment
import com.onion.ktatractcollection.shared.tools.KeyboardUtils
import com.onion.ktatractcollection.shared.tools.TextChangedWatcher
import java.text.DateFormat
import java.util.*

class TractFragment : Fragment(), DatePickerFragment.Callbacks {

    /**
     * Requests, Parameters, Dialog, ...
     */
    private enum class Requests {
        DATE
    }

    private enum class Dialogs(val description: String) {
        DATE("date_dialog")
    }

    /**
     * Properties
     */

    private lateinit var authorTextField: EditText
    private lateinit var dateButton: Button
    private lateinit var commentsTextField: EditText

    /* View Model */
    private val tractViewModel: TractViewModel by lazy {
        ViewModelProvider(this).get(TractViewModel::class.java)
    }
    private lateinit var tract: Tract

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        tract = Tract()

        arguments?.let {
            val args = TractFragmentArgs.fromBundle(it)
            args.tractId?.let { tractId -> tractViewModel.loadTract(UUID.fromString(tractId)) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.tract_fragment, container, false)
        setupOutlets(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tractViewModel.tract.observe(
            viewLifecycleOwner,
            { tract ->
                tract?.let {
                    this.tract = it
                    updateView(it)
                }
            }
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateView(tract)
    }

    override fun onStart() {
        setupListeners()
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        tractViewModel.saveTract(tract)
    }

    /**
     * Menu
     */

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.tract_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.save_tract_item -> {
                KeyboardUtils.hideKeyboard(requireActivity())
                requireActivity().onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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

    /**
     * Callbakcs
     */

    override fun onDateSelected(date: Date) {
        tract.discoveryDate = date
        updateView(tract)
    }

}
