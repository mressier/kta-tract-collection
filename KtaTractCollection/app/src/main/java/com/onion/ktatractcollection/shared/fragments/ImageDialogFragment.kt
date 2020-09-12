package com.onion.ktatractcollection.shared.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.onion.ktatractcollection.R
import java.io.File

private const val PARAM_FILE_ID = "file_id"
private const val PARAM_PATH_ID = "path_id"

class ImageDialogFragment: DialogFragment() {

    /**
     * Properties
     */
    /* Parameters */
    private var photoFile: File? = null
    private var photoPath: String? = null

    /* Outlets */
    private lateinit var photoView: ImageView

    /**
     * View Life Cycle
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val fileArgument = arguments?.getSerializable(PARAM_FILE_ID) as? File
        val pathArgument = arguments?.getSerializable(PARAM_PATH_ID) as? String
        fileArgument?.let {
            this.photoFile = fileArgument
            this.photoPath = pathArgument
        }

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
        photoFile?.path ?: photoPath ?.let {path ->
            updatePhoto(path)
        }
    }

    private fun updatePhoto(path: String) {
        Glide.with(context)
            .load(path)
            .asBitmap()
            .placeholder(R.drawable.ic_no_tract_photo)
            .into(photoView)
    }

    /**
     * Static methods
     */
    companion object {
        fun newInstanceFile(file: File): ImageDialogFragment {
            val args = Bundle().apply {
                putSerializable(PARAM_FILE_ID, file)
            }
            return ImageDialogFragment().apply { arguments = args }
        }

        fun newInstancePath(path: String): ImageDialogFragment {
            val args = Bundle().apply {
                putSerializable(PARAM_PATH_ID, path)
            }
            return ImageDialogFragment().apply { arguments = args }
        }
    }
}