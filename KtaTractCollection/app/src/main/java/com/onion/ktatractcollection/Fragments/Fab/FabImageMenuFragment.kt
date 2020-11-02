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
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.onion.ktatractcollection.R
import com.onion.ktatractcollection.shared.extensions.*
import kotlinx.android.synthetic.main.fragment_fab_image_menu.*
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

    private lateinit var fabClock: Animation
    private lateinit var fabAntiClock: Animation

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
        setupAnimations()
        return inflater.inflate(R.layout.fragment_fab_image_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()

        if (imageMenuViewModel.isMenuVisible) {
            showMenu(animated = false)
        } else {
            hideMenu(animated = false)
        }
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

    private fun savePicture() {
        val picture = imageMenuViewModel.savePictureFile()
        hideMenu()
        revokeCameraPermission()

        picture?.let { callbacks?.onTractSaved(picture.tractId) }
    }

    private fun savePictures(intent: Intent) {
        val picturesUri = intent.dataAsUriArray()

        picturesUri.forEach { uri ->
            val dest = imageMenuViewModel.generatePictureFile().toUri()
            requireContext().contentResolver.copy(uri, dest)
        }

        imageMenuViewModel.tractId?.let { callbacks?.onTractSaved(it) }

        hideMenu()
    }

    /**
     * Menu
     */

    private fun showMenu(animated: Boolean = true) {
        val duration: Long = if (animated) { 300 } else { 0 }

        imageMenuViewModel.isMenuVisible = true

        fabClock.duration = duration
        newTractButton.startAnimation(fabClock)

        galleryButton.show()
        galleryText.visibility = View.VISIBLE

        cameraButton.show()
        cameraText.visibility = View.VISIBLE

        backgroundView.isClickable = true

        backgroundView.animate().alpha(0.7F).duration = duration
    }

    private fun hideMenu(animated: Boolean = true) {
        val duration: Long = if (animated) { 300 } else { 0 }

        imageMenuViewModel.isMenuVisible = false

        fabAntiClock.duration = duration
        newTractButton.startAnimation(fabAntiClock)

        galleryButton.hide()
        galleryText.visibility = View.GONE

        cameraButton.hide()
        cameraText.visibility = View.GONE

        backgroundView.isClickable = false

        backgroundView.animate().alpha(0F).duration = duration
    }

    /**
     * Picture
     */

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

    /**
     * Gallery
     */

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

        cameraButton.setOnClickListener { takePicture() }

        galleryButton.setOnClickListener { importFromGallery() }
    }

    /**
     * Companion
     */
    companion object {
        private const val REQUEST_CAMERA_INTENT = 0
        private const val REQUEST_GALLERY_INTENT = 1

        private const val TAG = "FabImageMenuFgmt"
    }
}