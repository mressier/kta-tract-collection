package com.onion.ktatractcollection.Fragments.TractPictures

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.onion.ktatractcollection.Fragments.Tract.TractViewModel
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.R
import com.onion.ktatractcollection.shared.fragments.ImageDialogFragment
import com.onion.ktatractcollection.shared.tools.buildCameraIntent
import com.onion.ktatractcollection.shared.tools.isIntentAvailable
import java.io.File
import java.util.*


interface PictureListCallbacks {
    fun onPictureSelected(picture: File)
    fun onLastButtonSelected()
}

/**
 * A fragment representing a list of Items.
 */
class PicturesListFragment : Fragment(), PictureListCallbacks {

    /**
     * Properties
     */
    private lateinit var tractId: UUID
    private lateinit var photoFile: File
    private lateinit var photoUri: Uri

    /* View Model */
    private val tractViewModel: TractViewModel by lazy {
        ViewModelProvider(this).get(TractViewModel::class.java)
    }

    private val cameraIntent: Intent by lazy {
        requireActivity().buildCameraIntent(photoUri)
    }

    /* Outlets */
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PictureListAdapter

    /**
     * View Life Cycle
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    private fun revokeCameraPermission() {
        requireActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }

    /**
     * Update
     */
    fun updateTract(tractId: UUID) {
        this.tractId = tractId
        tractViewModel.loadTract(tractId)
    }

    private fun updateUI() {
        adapter.submitList(listOf(photoFile, photoFile, photoFile, photoFile, photoFile, photoFile, photoFile))
    }

    /**
     * Setup
     */
    private fun setupViewModel() {
        tractViewModel.tract.observe(
            viewLifecycleOwner,
            {
                val tract = it ?: Tract()
                photoFile = tractViewModel.getPhotoFile(tract)
                photoUri = FileProvider.getUriForFile(requireActivity(),
                    "com.onion.android.ktatractcollection.fileprovider",
                    photoFile)
                updateUI()
            }
        )
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

    override fun onPictureSelected(picture: File) {
        val intent = ImageDialogFragment.newInstance(picture)
        intent.show(requireActivity().supportFragmentManager, "show_picture") // TODO: add dialogs enum
    }

    override fun onLastButtonSelected() {
        if (!requireActivity().isIntentAvailable(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY)) {
            Toast.makeText(context, "No camera available", Toast.LENGTH_SHORT).show()
            return
        }

        startActivity(cameraIntent)
    }
}