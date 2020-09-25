package com.onion.ktatractcollection.Fragments.Tract

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.onion.ktatractcollection.Fragments.PicturesList.PicturesListFragment
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.R
import com.onion.ktatractcollection.shared.fragments.DatePickerFragment
import com.onion.ktatractcollection.shared.tools.*
import java.text.DateFormat
import java.util.*

class TractFragment : Fragment(), DatePickerFragment.Callbacks {

    /**
     * Properties
     */

    private lateinit var authorTextField: EditText
    private lateinit var discoveryDateButton: Button
    private lateinit var datingButton: Button
    private lateinit var commentsTextField: EditText
    private lateinit var picturesFragment: PicturesListFragment

    /* View Model */
    private val tractViewModel: TractViewModel by lazy {
        ViewModelProvider(this).get(TractViewModel::class.java)
    }
    private lateinit var tract: Tract

    /**
     * View Life Cycle
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        tract = Tract()

        arguments?.let {
            val args = TractFragmentArgs.fromBundle(it)
            args.tractId.let { tractId -> tractViewModel.loadTract(UUID.fromString(tractId)) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tract, container, false)
        setupOutlets(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tractViewModel.tract.observe(
            viewLifecycleOwner,
            {
                val tract = it ?: Tract()
                updateTract(tract)
                updateUI()
            }
        )
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
                requireActivity().hideKeyboard()
                requireActivity().onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Update
     */

    private fun updateUI() {
        authorTextField.setText(tract.author)
        commentsTextField.setText(tract.comment)

        val dateInstance =  DateFormat.getDateInstance(DateFormat.LONG)
        discoveryDateButton.text = dateInstance.format(tract.discoveryDate)
        datingButton.text = tract.dating?.let { dateInstance.format(it) } ?: "unknwon"
    }

    private fun updateTract(tract: Tract) {
        this.tract = tract
        picturesFragment.updateTract(tract.id)
    }

    /**
     * Setup
     */

    private fun setupListeners() {
        setupAuthorListener()
        setupCommentsListener()
        setupDiscoveryDateListener()
        setupDatingButtonListener()
    }

    private fun setupDiscoveryDateListener() {
        discoveryDateButton.setOnClickListener {
            showDatePickerDialog(tract.discoveryDate, Date(), REQUEST_DISCOVERY_DATE, DIALOG_DISCOVERY_DATE)
        }
    }

    private fun setupDatingButtonListener() {
        datingButton.setOnClickListener {
            showDatePickerDialog(tract.dating ?: Date(), null, REQUEST_DATING, DIALOG_DATING)
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
        discoveryDateButton = view.findViewById(R.id.discovery_date_button)
        datingButton = view.findViewById(R.id.dating_button)
        commentsTextField = view.findViewById(R.id.comments_text_field)

        val picturesFragment = childFragmentManager.findFragmentById(R.id.pictures_fragment)
        if (picturesFragment is PicturesListFragment) {
            this.picturesFragment = picturesFragment
        }
    }

    /**
     * Tools
     */

    private fun showDatePickerDialog(date: Date?, maxDate: Date?, requestId: Int, requestDescription: String) {
        DatePickerFragment.newInstance(date, maxDate, requestId).apply {
            setTargetFragment(this@TractFragment, requestId)
            show(this@TractFragment.requireFragmentManager(), requestDescription)
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
        updateUI()
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
