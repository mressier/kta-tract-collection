package com.onion.ktatractcollection.Fragments.Tract

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.Fragments.TractPictures.PicturesFragment
import com.onion.ktatractcollection.R
import com.onion.ktatractcollection.shared.fragments.DatePickerFragment
import com.onion.ktatractcollection.shared.fragments.ImageDialogFragment
import com.onion.ktatractcollection.shared.tools.*
import java.io.File
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
        DATE("date_dialog"),
        PICTURE("picture_dialog")
    }

    /**
     * Properties
     */

    private lateinit var authorTextField: EditText
    private lateinit var dateButton: Button
    private lateinit var commentsTextField: EditText
    private lateinit var pictureView: ImageView
    private lateinit var pictureButton: Button
    private lateinit var picturesFragment: Fragment

    /* View Model */
    private val tractViewModel: TractViewModel by lazy {
        ViewModelProvider(this).get(TractViewModel::class.java)
    }
    private lateinit var tract: Tract
    private lateinit var tractPhotoFile: File
    private lateinit var tractPhotoUri: Uri

    private val cameraIntent: Intent by lazy {
        requireActivity().buildCameraIntent(tractPhotoUri)
    }

    /**
     * View Life Cycle
     */

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

    override fun onDetach() {
        super.onDetach()
        revokeCameraPermission()
    }

    private fun revokeCameraPermission() {
        requireActivity().revokeUriPermission(tractPhotoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
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
        dateButton.text = DateFormat.getDateInstance(DateFormat.LONG).format(tract.discoveryDate)
        commentsTextField.setText(tract.comment)

        pictureButton.isEnabled =
            requireActivity().isIntentAvailable(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY)
        setupFragments()
    }

    private fun updateTract(tract: Tract) {
        this.tract = tract
        this.tractPhotoFile = tractViewModel.getPhotoFile(tract)
        this.tractPhotoUri = FileProvider.getUriForFile(requireActivity(),
            "com.onion.android.ktatractcollection.fileprovider",
            tractPhotoFile)
        updatePhoto()
    }

    private fun updatePhoto() {
        Glide.with(context)
            .load(tractPhotoFile)
            .asBitmap()
            .centerCrop()
            .placeholder(R.drawable.ic_no_tract_photo)
            .into(pictureView)

        pictureView.isEnabled = tractPhotoFile.exists()
    }

    /**
     * Setup
     */

    private fun setupListeners() {
        setupAuthorListener()
        setupCommentsListener()
        setupDateListener()
        setupPictureButtonListener()
        setupPictureViewListener()
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

    private fun setupPictureButtonListener() {
        pictureButton.setOnClickListener {
            startActivity(cameraIntent)
        }
    }

    private fun setupPictureViewListener() {
        pictureView.setOnClickListener {
            val intent = ImageDialogFragment.newInstance(tractPhotoFile)
            intent.show(requireActivity().supportFragmentManager, Dialogs.PICTURE.description)
        }
    }

    private fun setupOutlets(view: View) {
        authorTextField = view.findViewById(R.id.author_text_field)
        dateButton = view.findViewById(R.id.date_button)
        commentsTextField = view.findViewById(R.id.comments_text_field)
        pictureView = view.findViewById(R.id.picture_view)
        pictureButton = view.findViewById(R.id.picture_button)
    }

    private fun setupFragments() {
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentContainer = fragmentManager.findFragmentById(R.id.pictures_fragment)

        if (fragmentContainer == null) {
            val fragment = PicturesFragment.newInstance(tract.id)

            fragmentManager
                .beginTransaction()
                .add(R.id.pictures_fragment, fragment)
                .commit()
        }
    }

    /**
     * Callbakcs
     */

    override fun onDateSelected(date: Date) {
        tract.discoveryDate = date
        updateUI()
    }

}
