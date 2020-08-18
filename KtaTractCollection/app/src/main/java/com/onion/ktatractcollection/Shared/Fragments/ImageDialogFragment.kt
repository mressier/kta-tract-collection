package com.onion.ktatractcollection.Shared.Fragments

import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.onion.ktatractcollection.Shared.Tools.getScaledBitmap
import com.onion.ktatractcollection.R
import java.io.File

private const val fileId = "file_id"

class ImageDialogFragment: DialogFragment() {

    /**
     * Properties
     */
    /* Parameters */
    private lateinit var photoFile: File

    /* Outlets */
    private lateinit var photoView: ImageView

    /**
     * View Life Cycle
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val fileArgument = arguments?.getSerializable(fileId) as? File
        fileArgument?.let { this.photoFile = fileArgument }

        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_photo, container, false)
        setupView(view)
        return view
    }

    /**
     * Setup
     */
    private fun setupView(view: View) {
        photoView = view.findViewById(R.id.photo_view)
        updateUI()
    }
    
    private fun updateUI() {
        updatePhoto()
    }

    private fun updatePhoto() {
        if (photoFile.exists()) {
            val bitmap = getScaledBitmap(photoFile.path, requireActivity())
            updatePhotoView(bitmap)
        } else {
            photoView.setImageDrawable(null)
        }
    }

    private fun updatePhotoView(bitmap: Bitmap) {
        photoView.setImageBitmap(bitmap)

        photoView.layoutParams?.width = bitmap.width
        photoView.layoutParams?.height = bitmap.height
    }

    /**
     * Static methods
     */
    companion object {
        fun newInstance(file: File): ImageDialogFragment {
            val args = Bundle().apply {
                putSerializable(fileId, file)
            }
            return ImageDialogFragment().apply { arguments = args }
        }
    }
}