package com.unicorpdev.ktatract.shared.fragments.imagePager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.unicorpdev.ktatract.R
import kotlinx.android.synthetic.main.fragment_image_view.*
import java.io.File

class ImageViewFragment: Fragment() {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private val viewModel: ImageViewModel by lazy {
        ViewModelProvider(this).get(ImageViewModel::class.java)
    }

    /**
     * View Life Cycle
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            viewModel.imagePath = it.getSerializable(PARAM_PATH_ID) as? String
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
    }

    /**
     * Setup
     */
    
    private fun updateUI() {
        viewModel.getPictureFile()?.let { updatePhoto(it) }
    }

    private fun updatePhoto(file: File) {
        Glide.with(requireContext())
            .load(file)
            .centerInside()
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .into(photoView)
    }

    /**
     * Static methods
     */

    companion object {

        private const val PARAM_PATH_ID = "path_id"

        fun newInstance(path: String): ImageViewFragment {
            val args = Bundle().apply {
                putSerializable(PARAM_PATH_ID, path)
            }
            return ImageViewFragment().apply { arguments = args }
        }
    }
}