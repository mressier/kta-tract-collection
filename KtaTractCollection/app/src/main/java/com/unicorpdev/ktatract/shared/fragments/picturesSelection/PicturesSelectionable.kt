package com.unicorpdev.ktatract.shared.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.shared.extensions.*

enum class PictureSelectionMethod {
    GALLERY, CAMERA
}

abstract class PicturesSelectionable: Fragment() {

    abstract fun onPictureSelected(pictures: List<Uri>)

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private val viewModel by viewModels<PicturesSelectionableViewModel>()

    /***********************************************************************************************
     * View Life Cycle
     **********************************************************************************************/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_GALLERY_INTENT -> data?.let { savePictures(it) }
                REQUEST_CAMERA_INTENT -> savePicture()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    /***********************************************************************************************
     * Methods
     **********************************************************************************************/

    fun startPictureSelection(method: PictureSelectionMethod,
                              allowMultipleSelection: Boolean = true) {
        when (method) {
            PictureSelectionMethod.CAMERA -> takePicture()
            PictureSelectionMethod.GALLERY -> importFromGallery(allowMultipleSelection)
        }
    }

    /***********************************************************************************************
     * Camera
     **********************************************************************************************/

    /** Begin **/

    private fun takePicture() {
        checkCameraPermission(
            onPermissionGranted = { startCameraIntent() }
        )
    }

    private fun startCameraIntent() {
        val pictureFile = viewModel.generatePictureFile()

        val cameraIntent = requireActivity().buildCameraIntentForFile(pictureFile)
        startActivityForResult(cameraIntent, REQUEST_CAMERA_INTENT)
    }

    /** End **/

    private fun savePicture() {
        revokeCameraPermission()

        val pictureUri = viewModel.pictureFile?.toUri()
        val picturesUri = pictureUri?.let { listOf(it) } ?: listOf()

        onPictureSelected(picturesUri)
    }

    /***********************************************************************************************
     * Gallery
     **********************************************************************************************/

    /** Begin **/

    private fun importFromGallery(allowMultipleSelection: Boolean) {
        val galleryIntent = buildGalleryIntent(
            retainDocument = true,
            allowMultipleFiles = allowMultipleSelection
        )
        startActivityForResult(galleryIntent, REQUEST_GALLERY_INTENT)
    }

    /** End **/

    private fun savePictures(intent: Intent) {
        val picturesUri = intent.dataAsUriArray()

        // Picture are duplicated from device to the application
        val picturesCopyUri = picturesUri.map { uri ->
            val dest = viewModel.generatePictureFile().toUri()
            requireContext().contentResolver.copy(uri, dest)
            dest
        }

        revokeCameraPermission()

        onPictureSelected(picturesCopyUri)
    }

    /***********************************************************************************************
     * Permission
     **********************************************************************************************/

    fun revokeCameraPermission() {
        viewModel.pictureFile?.toUri()?.let { uri ->
            requireActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
    }

    /***********************************************************************************************
     * Companion
     **********************************************************************************************/
    
    companion object {
        private const val REQUEST_CAMERA_INTENT = 152
        private const val REQUEST_GALLERY_INTENT = 153
    }
}

class PicturesSelectionFragment: PicturesSelectionable() {

    interface Callbacks {
        fun onPicturesSelected(pictures: List<Uri>)
    }

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private var callbacks: Callbacks? = null

    /***********************************************************************************************
     * View Life Cycle
     **********************************************************************************************/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pictures_selection, container, false)
    }

    override fun onAttach(context: Context) {
        callbacks = context as? Callbacks ?: parentFragment as? Callbacks
        super.onAttach(context)
    }

    override fun onDetach() {
        callbacks = null
        super.onDetach()
    }

    /***********************************************************************************************
     * Callbacks
     **********************************************************************************************/

    override fun onPictureSelected(pictures: List<Uri>) {
        callbacks?.onPicturesSelected(pictures)
    }

}