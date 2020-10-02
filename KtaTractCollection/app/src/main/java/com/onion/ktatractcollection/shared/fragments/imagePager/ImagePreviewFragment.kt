package com.onion.ktatractcollection.shared.fragments.imagePager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.github.chrisbanes.photoview.PhotoView
import com.onion.ktatractcollection.R

class ImagePreviewFragment: Fragment() {

    /**
     * Properties
     */
    /* Parameters */
    private var photoPath: String? = null

    /* Outlets */
    private lateinit var photoView: PhotoView

    /**
     * View Life Cycle
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            this.photoPath = it.getSerializable(PARAM_PATH_ID) as? String
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_image_preview, container, false)
        setupView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
    }

    /**
     * Setup
     */
    private fun setupView(view: View) {
        photoView = view.findViewById(R.id.photo_view)
    }
    
    private fun updateUI() {
        photoPath?.let { updatePhoto(it) }
    }

    private fun updatePhoto(path: String) {
        Glide.with(requireContext())
            .load(path)
            .centerInside()
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .into(photoView)
    }

    /**
     * Static methods
     */
    companion object {

        private const val PARAM_PATH_ID = "path_id"

        fun newInstance(path: String): ImagePreviewFragment {
            val args = Bundle().apply {
                putSerializable(PARAM_PATH_ID, path)
            }
            return ImagePreviewFragment().apply { arguments = args }
        }
    }
}