package com.onion.ktatractcollection.Fragments.TractPictures

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.onion.ktatractcollection.Models.TractPicture
import com.onion.ktatractcollection.R
import com.onion.ktatractcollection.shared.fragments.ImageDialogFragment
import com.onion.ktatractcollection.shared.tools.*
import java.io.File
import java.util.*

private const val TAG = "PicturesListFragment"
private const val REQUEST_CAMERA_PERMISSION = 0
private const val REQUEST_CAMERA_INTENT = 1


interface PictureListCallbacks {
    fun onPictureSelected(picture: File)
    fun onLastButtonSelected()
    fun onDeleteButtonSelected(picture: File)
}

/**
 * A fragment representing a list of Items.
 */
class PicturesListFragment : Fragment(), PictureListCallbacks, PermissionGrantedCallbacks {

    /**
     * Properties
     */
    private lateinit var tractId: UUID
    private lateinit var pictures: MutableList<TractPicture>

    /* View Model */
    private val picturesViewModel: TractPicturesViewModel by lazy {
        ViewModelProvider(this).get(TractPicturesViewModel::class.java)
    }

    /* Camera */
    private var cameraIntent: Intent? = null
    private var pictureUri: Uri? = null
    private var picture: TractPicture? = null

    /* Outlets */
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PictureListAdapter

    /**
     * View Life Cycle
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) { return }

        arguments?.let {
           updateTract(UUID.fromString(it.getString("tract_id")))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pictures_item_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            recyclerView = view
            adapter = PictureListAdapter(requireContext(), this)
            recyclerView.adapter = adapter
        }
        setupViewModel()
        return view
    }

    override fun onDetach() {
        super.onDetach()
        revokeCameraPermission()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA_INTENT) {
                picturesViewModel.savePicture(picture!!) // TODO
                revokeCameraPermission()
                picture = null
                pictureUri = null
                cameraIntent = null
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * Permission
     */

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                checkGrantResultForPermission(
                    android.Manifest.permission.CAMERA,
                    permissions,
                    grantResults,
                    this
                )
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onPermissionGranted(permission: String) {
        startCameraIntent(pictureUri ?: generatePhotoUriForTract())
    }

    override fun onPermissionDenied(permission: String) {
        println("canceled")
    }

    override fun onPermissionDeniedForever(permission: String) {
        requireContext().showPermissionAlwaysDeniedExplanation(
            getString(R.string.camera_permission_denied)
        )
    }

    private fun revokeCameraPermission() {
        requireActivity().revokeUriPermission(pictureUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }

    /**
     * Update
     */
    fun updateTract(tractId: UUID) {
        this.tractId = tractId
        picturesViewModel.loadPicturesForTractId(tractId)
    }

    private fun updateUI() {
        adapter.submitList(picturesViewModel.convertPicturesToFile(pictures))
    }

    /**
     * Setup
     */

    private fun setupViewModel() {
        picturesViewModel.pictures.observe(
            viewLifecycleOwner,
            {
                this.pictures = it.toMutableList()
                updateUI()
            }
        )
    }

    /**
     * Callbacks
     */

    override fun onPictureSelected(picture: File) {
        val intent = ImageDialogFragment.newInstance(picture)
        intent.show(requireActivity().supportFragmentManager, "show_picture") // TODO: add dialogs enum
    }

    override fun onLastButtonSelected() {
        startCameraIntent(generatePhotoUriForTract())
    }

    override fun onDeleteButtonSelected(picture: File) {
        pictures.find { it.photoFilename == picture.name }?.let { tractPicture ->
            Log.i(TAG, "Delete picture ${tractPicture.photoFilename}")
            picturesViewModel.deletePicture(tractPicture)
            pictures.remove(tractPicture)
            updateUI()
        }
    }

    private fun generatePhotoUriForTract(): Uri {
        val picture = TractPicture(tractId = tractId)
        val file = picturesViewModel.convertPictureToFile(picture)

        val photoUri = FileProvider.getUriForFile(
            requireActivity(),
            "com.onion.android.ktatractcollection.fileprovider",
            file
        )

        this.picture = picture

        return photoUri
    }

    /**
     * Camera
     */

    private fun startCameraIntent(photoUri: Uri) {
        if (shouldWaitForCameraPermission()) { return }

        val cameraIntent = requireActivity().buildCameraIntent(photoUri)
        if (!requireActivity().isIntentAvailable(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY)) {
            Toast.makeText(context, "No camera available", Toast.LENGTH_SHORT).show()
            return
        }

        this.pictureUri = photoUri
        this.cameraIntent = cameraIntent

        startActivityForResult(cameraIntent, REQUEST_CAMERA_INTENT)
    }

    private fun shouldWaitForCameraPermission(): Boolean {
        val permission = android.Manifest.permission.CAMERA

        if (!requireContext().permissionIsGranted(permission)) {
            requestPermissionWithRationale(
                requireContext(),
                getString(R.string.camera_permission_description),
                permission,
                REQUEST_CAMERA_PERMISSION
            ) { println("canceled") }
            return true
        }
        return false
    }

    /**
     * Initialize
     */

    companion object {

        @JvmStatic
        fun newInstance(tractId: UUID) =
            PicturesListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("tract_id", tractId.toString())
                }
            }
    }

}