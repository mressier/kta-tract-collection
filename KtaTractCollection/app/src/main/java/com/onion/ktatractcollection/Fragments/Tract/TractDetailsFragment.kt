package com.onion.ktatractcollection.Fragments.Tract

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.R
import com.onion.ktatractcollection.shared.fragments.DatePickerFragment
import com.onion.ktatractcollection.shared.tools.TextChangedWatcher
import java.text.DateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TractDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TractDetailsFragment : Fragment(), DatePickerFragment.Callbacks {

    /**
     * Properties
     */

    /* Outlets */
    private lateinit var authorTextField: EditText
    private lateinit var discoveryDateButton: Button
    private lateinit var datingButton: Button
    private lateinit var commentsTextField: EditText

    /* View Model */
    private val tractViewModel: TractViewModel by lazy {
        ViewModelProvider(this).get(TractViewModel::class.java)
    }

    private lateinit var tract: Tract


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tract = Tract()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tract_details, container, false)
        setupOutlets(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModelObserver()
        setupListeners()
    }

    override fun onStop() {
        super.onStop()
        tractViewModel.saveTract(tract)
    }
    /**
     * Methods
     */

    fun setTract(tractId: UUID) {
        tractViewModel.loadTract(tractId)
    }

    fun updateUI(tract: Tract) {
        this.tract = tract
        authorTextField.setText(tract.author)
        commentsTextField.setText(tract.comment)

        val dateInstance =  DateFormat.getDateInstance(DateFormat.LONG)
        discoveryDateButton.text = dateInstance.format(tract.discoveryDate)
        datingButton.text = tract.dating?.let { dateInstance.format(it) } ?: "UNKNOWN"
    }

    /**
     * Setup
     */

    private fun setupOutlets(view: View) {
        authorTextField = view.findViewById(R.id.author_text_field)
        discoveryDateButton = view.findViewById(R.id.discovery_date_button)
        datingButton = view.findViewById(R.id.dating_button)
        commentsTextField = view.findViewById(R.id.comments_text_field)
    }

    private fun setupViewModelObserver() {
        tractViewModel.tract.observe(
            viewLifecycleOwner,
            {
                val tract = it ?: Tract()
                updateUI(tract)
            }
        )
    }

    private fun setupListeners() {
        setupAuthorListener()
        setupCommentsListener()
        setupDiscoveryDateListener()
        setupDatingButtonListener()
    }

    private fun setupDiscoveryDateListener() {
        discoveryDateButton.setOnClickListener {
            showDatePickerDialog(tract.discoveryDate, Date(),
                REQUEST_DISCOVERY_DATE,
                DIALOG_DISCOVERY_DATE
            )
        }
    }

    private fun setupDatingButtonListener() {
        datingButton.setOnClickListener {
            showDatePickerDialog(tract.dating ?: Date(), null,
                REQUEST_DATING,
                DIALOG_DATING
            )
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

    /**
     * Tools
     */

    private fun showDatePickerDialog(date: Date?, maxDate: Date?, requestId: Int, requestDescription: String) {
        DatePickerFragment.newInstance(date, maxDate, requestId).apply {
            setTargetFragment(this@TractDetailsFragment, requestId)
            show(this@TractDetailsFragment.requireFragmentManager(), requestDescription)
        }
    }

    /**
     * Callbakcs
     */

    override fun onDateSelected(date: Date, requestId: Int) {
        when (requestId) {
            REQUEST_DISCOVERY_DATE -> tract.discoveryDate = date
            REQUEST_DATING -> tract.dating = date
        }
        updateUI(tract)
    }

    /**
     * Companion
     */
    companion object {

        private const val REQUEST_DISCOVERY_DATE = 0
        private const val REQUEST_DATING = 1

        private const val DIALOG_DISCOVERY_DATE = "dialog_discovery_date"
        private const val DIALOG_DATING = "dialog_dating"
    }
}