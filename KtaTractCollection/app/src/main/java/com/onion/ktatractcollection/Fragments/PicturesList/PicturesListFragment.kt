package com.onion.ktatractcollection.Fragments.PicturesList

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.onion.ktatractcollection.Models.TractPicture
import com.onion.ktatractcollection.R
import com.onion.ktatractcollection.shared.fragments.ImageDialogFragment
import com.onion.ktatractcollection.shared.tools.*
import java.util.*

private const val TAG = "PicturesListFragment"
private const val REQUEST_CAMERA_PERMISSION = 0
private const val REQUEST_PICTURE_SELECTION_DIALOG = 1
private const val REQUEST_CAMERA_INTENT = 2
private const val REQUEST_GALLERY_INTENT = 3

private const val DIALOG_SELECT_PICTURE = "select_picture"
private const val DIALOG_SHOW_PICTURE = "show_picture"


interface PictureListCallbacks {
    fun onPictureSelected(path: String)
    fun onLastButtonSelected()
    fun onDeleteButtonSelected(path: String)
}

/**
 * A fragment representing a list of Items.
 */
class PicturesListFragment : Fragment(), PictureListCallbacks, PermissionGrantedCallbacks, PictureSelectionDialogFragment.Callbacks {

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

    /* Outlets */
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PicturesListAdapter

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
        val view = inflater.inflate(R.layout.fragment_pictures_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            recyclerView = view
            adapter = PicturesListAdapter(requireContext(), this)
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
            when (requestCode) {
                REQUEST_CAMERA_INTENT -> savePictureFromCameraIntent()
                REQUEST_GALLERY_INTENT -> data?.let { savePictureFromGalleryIntent(it) }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun savePictureFromGalleryIntent(data: Intent) {
        val selectedImage = data.data ?: run { return }
        savePictureWithUri(selectedImage, true)

    }

    private fun savePictureFromCameraIntent() {
        pictureUri?.let { uri -> savePictureWithUri(uri, false) }

        revokeCameraPermission()
        pictureUri = null
        cameraIntent = null
    }

    private fun savePictureWithUri(uri: Uri, isFromDevice: Boolean) {
        val picture = TractPicture(
            tractId = tractId,
            photoFilename = "$uri",
            isFromDevice = isFromDevice
        )
        picturesViewModel.savePicture(picture)
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
        adapter.submitList(picturesViewModel.convertPicturesToPath(pictures))
    }

    /**
     * Setup
     */

    private fun setupViewModel() {
        picturesViewModel.pictures.observe(
            viewLifecycleOwner,
            {
                this.pictures = it.toMutableList()
                println("------- get pictures ${pictures.map { it.photoFilename }}")
                updateUI()
            }
        )
    }

    /**
     * Callbacks
     */

    override fun onPictureSelected(path: String) {
        val intent = ImageDialogFragment.newInstancePath(path)
        intent.show(requireActivity().supportFragmentManager, DIALOG_SHOW_PICTURE)
    }

    override fun onLastButtonSelected() {
        startPictureSelectionIntent()
    }

    override fun onDeleteButtonSelected(path: String) {
        pictures.find { it.photoFilename == path }?.let { tractPicture ->
            Log.i(TAG, "Delete picture ${tractPicture.photoFilename}")
            picturesViewModel.deletePicture(tractPicture)
            pictures.remove(tractPicture)
            updateUI()
        }
    }

    private fun generatePhotoUriForTract(): Uri {
        val picture = TractPicture(tractId = tractId, isFromDevice = false)

        val file = picturesViewModel.convertPictureToFile(picture)
        val photoUri = FileProvider.getUriForFile(
            requireActivity(),
            getString(R.string.app_fileprovider_authority),
            file
        )

        Log.d(TAG, "Generate photo uri $photoUri")
        return photoUri
    }

    private fun startPictureSelectionIntent() {
        PictureSelectionDialogFragment.newInstance().apply {
            setTargetFragment(this@PicturesListFragment, REQUEST_PICTURE_SELECTION_DIALOG)
            show(this@PicturesListFragment.requireFragmentManager(), DIALOG_SELECT_PICTURE)
        }
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

    override fun onCameraOptionSelected() {
        startCameraIntent(generatePhotoUriForTract())
    }

    override fun onGalleryOptionSelected() {
        val galleryIntent = buildGalleryIntent(retainDocument = true)
        startActivityForResult(galleryIntent, REQUEST_GALLERY_INTENT)
    }

}