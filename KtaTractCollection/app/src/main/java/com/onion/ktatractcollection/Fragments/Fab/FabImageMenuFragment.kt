package com.onion.ktatractcollection.Fragments.Fab

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.onion.ktatractcollection.R
import com.onion.ktatractcollection.shared.tools.buildCameraIntentForFile
import com.onion.ktatractcollection.shared.tools.buildGalleryIntent
import com.onion.ktatractcollection.shared.tools.checkCameraPermission
import com.onion.ktatractcollection.shared.tools.dataAsUriArray
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class FabImageMenuFragment : Fragment() {

    interface Callbacks {
        fun onTractSaved(tractId: UUID)
    }

    /**
     * Properties
     */

    /* Outlets */
    private lateinit var newTractButton: FloatingActionButton
    private lateinit var galleryButton: FloatingActionButton
    private lateinit var galleryText: TextView
    private lateinit var cameraButton: FloatingActionButton
    private lateinit var cameraText: TextView
    private lateinit var backgroundView: ImageView

    private lateinit var fabOpen: Animation
    private lateinit var fabClose: Animation
    private lateinit var fabClock: Animation
    private lateinit var fabAnticlock: Animation

    /* View Models */
    private val imageMenuViewModel: FabImageMenuViewModel by lazy {
        ViewModelProvider(this).get(FabImageMenuViewModel::class.java)
    }

    var callbacks: Callbacks? = null

    /**
     * View Life Cycle
     */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fab_image_menu, container, false)
        setupView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()

        if (imageMenuViewModel.isMenuVisible) { showMenu(animated = false) } else { hideMenu(
            animated = false
        ) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
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
        super.onDetach()
        callbacks = null
        revokeCameraPermission()
    }

    /**
     * Methods
     */

    fun setTract(tractId: UUID?) {
        imageMenuViewModel.tractId = tractId
    }

    private fun savePicture() {
        val picture = imageMenuViewModel.savePictureFile()
        hideMenu()
        revokeCameraPermission()

        picture?.let { callbacks?.onTractSaved(picture.tractId) }
    }

    private fun savePictures(intent: Intent) {
        val picturesUri = intent.dataAsUriArray()
        val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION

        picturesUri.forEach {
            context?.contentResolver?.takePersistableUriPermission(it, flags)
        }

        val pictures = imageMenuViewModel.savePictures(picturesUri)
        hideMenu()

        pictures.firstOrNull()?.let { callbacks?.onTractSaved(it.tractId) }
    }

    private fun showMenu(animated: Boolean = true) {
        imageMenuViewModel.isMenuVisible = true

        fabClock.duration = if (animated) { 300 } else { 0 }
        newTractButton.startAnimation(fabClock)

        galleryButton.show()
        galleryText.visibility = View.VISIBLE

        cameraButton.show()
        cameraText.visibility = View.VISIBLE

        backgroundView.isClickable = true

        fabOpen.duration = if (animated) { 300 } else { 0 }
        backgroundView.startAnimation(fabOpen)
    }

    private fun hideMenu(animated: Boolean = true) {
        imageMenuViewModel.isMenuVisible = false

        fabAnticlock.duration = if (animated) { 300 } else { 0 }
        newTractButton.startAnimation(fabAnticlock)

        galleryButton.hide()
        galleryText.visibility = View.GONE

        cameraButton.hide()
        cameraText.visibility = View.GONE

        backgroundView.isClickable = false

        fabClose.duration = if (animated) { 300 } else { 0 }
        backgroundView.startAnimation(fabClose)
    }

    private fun takePicture() {
        checkCameraPermission(onPermissionGranted = { startCameraIntent() })
    }

    private fun startCameraIntent() {
        val pictureFile = imageMenuViewModel.generatePictureFile()

        val cameraIntent = requireActivity().buildCameraIntentForFile(pictureFile)
        startActivityForResult(cameraIntent, REQUEST_CAMERA_INTENT)
    }

    private fun importFromGallery() {
        val galleryIntent = buildGalleryIntent(retainDocument = true, allowMultipleFiles = true)
        startActivityForResult(galleryIntent, REQUEST_GALLERY_INTENT)
    }

    private fun revokeCameraPermission() {
        imageMenuViewModel.pictureFile?.toUri()?.let { uri ->
            requireActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
    }

    /**
     * Setup
     */

    private fun setupView(view: View) {
        newTractButton = view.findViewById(R.id.add_button)
        galleryButton = view.findViewById(R.id.gallery_button)
        galleryText = view.findViewById(R.id.gallery_text)
        cameraButton = view.findViewById(R.id.camera_button)
        cameraText = view.findViewById(R.id.camera_text)
        backgroundView = view.findViewById(R.id.background_view)

        fabClose = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out);
        fabOpen = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in);
        fabClock = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_rotate_clock);
        fabAnticlock = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_rotate_anticlock);
    }

    private fun setupListeners() {
        newTractButton.setOnClickListener {
            if (imageMenuViewModel.isMenuVisible) { hideMenu(animated = true) } else { showMenu(
                animated = true
            ) }
        }

        backgroundView.setOnClickListener { hideMenu() }

        cameraButton.setOnClickListener { takePicture() }

        galleryButton.setOnClickListener { importFromGallery() }
    }

    /**
     * Companion
     */
    companion object {
        private const val REQUEST_CAMERA_INTENT = 0
        private const val REQUEST_GALLERY_INTENT = 1
    }
}