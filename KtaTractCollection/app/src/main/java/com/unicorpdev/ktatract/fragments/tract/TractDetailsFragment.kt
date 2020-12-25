package com.unicorpdev.ktatract.fragments.tract

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.unicorpdev.ktatract.models.Tract
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.fragments.collectionList.spinner.CollectionSpinnerFragment
import com.unicorpdev.ktatract.shared.fragments.DatePickerFragment
import com.unicorpdev.ktatract.shared.tools.TextChangedWatcher
import com.unicorpdev.ktatract.shared.extensions.longString
import kotlinx.android.synthetic.main.fragment_collection_spinner.*
import kotlinx.android.synthetic.main.fragment_tract_details.*
import java.util.*

class TractDetailsFragment : Fragment(),
    DatePickerFragment.Callbacks,
    CollectionSpinnerFragment.Callbacks
{

    /**
     * Properties
     */

    /* View Model */
    private val tractViewModel: TractViewModel by lazy {
        ViewModelProvider(this).get(TractViewModel::class.java)
    }

    private lateinit var spinnerFragment: CollectionSpinnerFragment

    /**
     * View Life cycle
     */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tract_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCollectionSpinner()
        setupViewModelObserver()
        setupListeners()
    }

    override fun onStop() {
        super.onStop()
        tractViewModel.saveTract()
    }

    /**
     * Methods
     */

    fun setTract(tractId: UUID) {
        tractViewModel.loadTract(tractId)
    }

    private fun updateUI() {
        val tract = tractViewModel.savedTract
        authorTextField.setText(tract.author)
        commentsTextField.setText(tract.comment)

        discoveryDateButton.text = tract.discoveryDate.longString
        datingButton.text = tract.dating?.longString ?: getString(R.string.unknown)
        spinnerFragment.selectedCollectionId = tract.collectionId
    }

    /**
     * Setup
     */

    private fun setupCollectionSpinner() {
        spinnerFragment =
            childFragmentManager.findFragmentById(R.id.spinnerFragment) as CollectionSpinnerFragment
    }

    private fun setupViewModelObserver() {
        tractViewModel.tract.observe(
            viewLifecycleOwner,
            {
                tractViewModel.savedTract = it ?: Tract()
                updateUI()
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
            showDatePickerDialog(tractViewModel.savedTract.discoveryDate, Date(),
                REQUEST_DISCOVERY_DATE,
                DIALOG_DISCOVERY_DATE
            )
        }
    }

    private fun setupDatingButtonListener() {
        datingButton.setOnClickListener {
            showDatePickerDialog(tractViewModel.savedTract.dating ?: Date(), null,
                REQUEST_DATING,
                DIALOG_DATING
            )
        }
    }

    private fun setupAuthorListener() {
        val authorWatcher = TextChangedWatcher() { tractViewModel.savedTract.author = it }
        authorTextField.addTextChangedListener(authorWatcher)
    }

    private fun setupCommentsListener() {
        val commentsWatcher = TextChangedWatcher() { tractViewModel.savedTract.comment = it }
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
            REQUEST_DISCOVERY_DATE -> tractViewModel.savedTract.discoveryDate = date
            REQUEST_DATING -> tractViewModel.savedTract.dating = date
        }
        updateUI()
    }

    override fun onCollectionSelected(collectionId: UUID) {
        tractViewModel.savedTract.collectionId = collectionId
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