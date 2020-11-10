package com.onion.ktatractcollection.Fragments.Tract

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.onion.ktatractcollection.shared.extensions.*
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.R
import com.onion.ktatractcollection.shared.fragments.DatePickerFragment
import com.onion.ktatractcollection.shared.tools.TextChangedWatcher
import kotlinx.android.synthetic.main.fragment_tract_details.*
import java.util.*

class TractDetailsFragment : Fragment(), DatePickerFragment.Callbacks {

    /**
     * Properties
     */

    /* View Model */
    private val tractViewModel: TractViewModel by lazy {
        ViewModelProvider(this).get(TractViewModel::class.java)
    }

    private lateinit var tract: Tract

    /**
     * View Life cycle
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tract = Tract()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tract_details, container, false)
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

    private fun updateUI(tract: Tract) {
        this.tract = tract
        authorTextField.setText(tract.author)
        commentsTextField.setText(tract.comment)

        discoveryDateButton.text = tract.discoveryDate.longString
        datingButton.text = tract.dating?.longString ?: getString(R.string.unknown)
    }

    /**
     * Setup
     */

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