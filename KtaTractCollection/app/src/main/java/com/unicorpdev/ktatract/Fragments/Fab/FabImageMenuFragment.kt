package com.unicorpdev.ktatract.Fragments.Fab

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.shared.analytics.KtaTractAnalytics
import com.unicorpdev.ktatract.shared.analytics.KtaTractAnalytics.SelectEvent
import com.unicorpdev.ktatract.shared.extensions.*
import com.unicorpdev.ktatract.shared.extensions.*
import kotlinx.android.synthetic.main.fragment_fab_image_menu.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class FabImageMenuFragment : Fragment() {

    interface Callbacks {
        fun onTractSaved(tractId: UUID)
        fun onTractsSaved(tractIds: Array<UUID>)
    }

    /**
     * Properties
     */

    private lateinit var fabClock: Animation
    private lateinit var fabAntiClock: Animation

    /* View Models */
    private val imageMenuViewModel: FabImageMenuViewModel by lazy {
        ViewModelProvider(this).get(FabImageMenuViewModel::class.java)
    }

    var callbacks: Callbacks? = null

    /**
     * Callbacks
     */

    private val interceptBackPressed: OnBackPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (imageMenuViewModel.isMenuVisible) { hideMenu(true) }
            }
        }
    }

    /**
     * View Life Cycle
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(interceptBackPressed)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupAnimations()
        return inflater.inflate(R.layout.fragment_fab_image_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupButtonsTexts()

        if (imageMenuViewModel.isMenuVisible) {
            showMenu(animated = false)
        } else {
            hideMenu(animated = false)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_MULTIPLE_IMPORT_INTENT -> data?.let { saveTracts(it) }
                REQUEST_GALLERY_INTENT -> data?.let { savePictures(it) }
                REQUEST_CAMERA_INTENT -> savePicture()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as? Callbacks
    }

    override fun onDetach() {
        callbacks = null
        revokeCameraPermission()
        super.onDetach()
    }

    /**
     * Methods
     */

    fun setTract(tractId: UUID?) {
        imageMenuViewModel.tractId = tractId
    }

    fun setShouldShowMultipleImportButton(shouldShowMultipleImportButton: Boolean) {
        imageMenuViewModel.shouldShowMultipleImport = shouldShowMultipleImportButton
    }
    
    /***********************************************************************************************
     * Tools
     **********************************************************************************************/

    private fun savePicture() {
        imageMenuViewModel.savePictureFile()

        hideMenu()
        revokeCameraPermission()

        imageMenuViewModel.tractId?.let { callbacks?.onTractSaved(it) }
    }

    private fun savePictures(intent: Intent) {
        val picturesUri = intent.dataAsUriArray()

        picturesUri.forEach { uri ->
            val dest = imageMenuViewModel.generatePictureFile().toUri()
            if (requireContext().contentResolver.copy(uri, dest)) {
                imageMenuViewModel.savePictureFile()
            }
        }

        imageMenuViewModel.tractId?.let { callbacks?.onTractSaved(it) }

        hideMenu()
    }

    private fun saveTracts(intent: Intent) {
        val picturesUri = intent.dataAsUriArray()

        val tracts = picturesUri.map { uri ->
            val tract = imageMenuViewModel.generateTract()
            val dest = imageMenuViewModel.generatePictureFile().toUri()

            if (requireContext().contentResolver.copy(uri, dest)) {
                imageMenuViewModel.savePictureFile()
            }

            tract
        }

        callbacks?.onTractsSaved(tracts.toTypedArray())

        hideMenu()
    }

    /***********************************************************************************************
     * Menu
     **********************************************************************************************/
    
    private fun showMenu(animated: Boolean = true) {
        val duration: Long = if (animated) { 300 } else { 0 }

        imageMenuViewModel.isMenuVisible = true
        interceptBackPressed.isEnabled = true

        fabClock.duration = duration
        newTractButton.startAnimation(fabClock)

        galleryButton.show()
        galleryText.visibility = View.VISIBLE

        cameraButton.show()
        cameraText.visibility = View.VISIBLE

        if (imageMenuViewModel.shouldShowMultipleImport) {
            multipleImportButton.show()
            multipleImportText.visibility = View.VISIBLE
        } else {
            multipleImportButton.hide()
            multipleImportText.visibility = View.GONE
        }

        backgroundView.isClickable = true

        backgroundView.animate().alpha(0.7F).duration = duration
    }

    private fun hideMenu(animated: Boolean = true) {
        val duration: Long = if (animated) { 300 } else { 0 }

        imageMenuViewModel.isMenuVisible = false
        interceptBackPressed.isEnabled = false

        fabAntiClock.duration = duration
        newTractButton.startAnimation(fabAntiClock)

        galleryButton.hide()
        galleryText.visibility = View.GONE

        cameraButton.hide()
        cameraText.visibility = View.GONE

        multipleImportButton.hide()
        multipleImportText.visibility = View.GONE

        backgroundView.isClickable = false

        backgroundView.animate().alpha(0F).duration = duration
    }

    /***********************************************************************************************
     * Take Picture
     **********************************************************************************************/
    
    private fun takePicture() {
        checkCameraPermission(onPermissionGranted = { startCameraIntent() })
        hideMenu(animated = false)
    }

    private fun startCameraIntent() {
        val pictureFile = imageMenuViewModel.generatePictureFile()

        val cameraIntent = requireActivity().buildCameraIntentForFile(pictureFile)
        startActivityForResult(cameraIntent, REQUEST_CAMERA_INTENT)
        hideMenu(animated = false)
    }

    /***********************************************************************************************
     * Gallery
     **********************************************************************************************/
    
    private fun importFromGallery() {
        val galleryIntent = buildGalleryIntent(retainDocument = true, allowMultipleFiles = true)
        startActivityForResult(galleryIntent, REQUEST_GALLERY_INTENT)
    }

    private fun revokeCameraPermission() {
        imageMenuViewModel.pictureFile?.toUri()?.let { uri ->
            requireActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
    }

    /***********************************************************************************************
     * Multiple Gallery
     **********************************************************************************************/

    private fun multipleImportFromGallery() {
        val galleryIntent = buildGalleryIntent(retainDocument = true, allowMultipleFiles = true)
        startActivityForResult(galleryIntent, REQUEST_MULTIPLE_IMPORT_INTENT)
    }

    /***********************************************************************************************
     * Setup
     **********************************************************************************************/

    private fun setupAnimations() {
        fabClock = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_rotate_clock);
        fabAntiClock = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_rotate_anticlock);
    }

    private fun setupListeners() {
        newTractButton.setOnClickListener {
            if (imageMenuViewModel.isMenuVisible) {
                hideMenu(animated = true)
            } else {
                showMenu(animated = true)
            }
        }

        backgroundView.setOnClickListener { hideMenu() }

        cameraButton.setOnClickListener {
            KtaTractAnalytics.logSelectItem(SelectEvent.IMPORT_ONE_PICTURE)
            takePicture()
        }

        galleryButton.setOnClickListener {
            KtaTractAnalytics.logSelectItem(SelectEvent.IMPORT_ONE_GALLERY)
            importFromGallery()
        }

        multipleImportButton.setOnClickListener {
            KtaTractAnalytics.logSelectItem(SelectEvent.IMPORT_MULTIPLE)
            multipleImportFromGallery()
        }
    }

    private fun setupButtonsTexts() {
        val galleryButtonText = if (imageMenuViewModel.tractId == null)
            R.string.fab_btn_import_tract_from_gallery
        else R.string.fab_btn_import_image_from_gallery

        val cameraButtonText = if (imageMenuViewModel.tractId == null)
            R.string.fab_btn_import_tract_take_picture
        else R.string.fab_btn_import_image_take_picture

        multipleImportText.text = getString(R.string.fab_btn_multiple_import)
        galleryText.text = getString(galleryButtonText)
        cameraText.text = getString(cameraButtonText)
    }

    /**
     * Companion
     */
    companion object {
        private const val REQUEST_CAMERA_INTENT = 0
        private const val REQUEST_GALLERY_INTENT = 1
        private const val REQUEST_MULTIPLE_IMPORT_INTENT = 2

        private val TAG = FabImageMenuFragment::class.simpleName ?: "Default"
    }
}
