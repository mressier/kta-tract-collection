package com.onion.ktatractcollection.Fragments.PicturesList

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.onion.ktatractcollection.Fragments.TractList.dialogs.TractDialogFragment
import com.onion.ktatractcollection.R

/**
 * A simple [Fragment] subclass.
 * Use the [PictureSelectionDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PictureSelectionDialogFragment : DialogFragment() {

    interface Callbacks {
        fun onPictureSelected()
        fun onGallerySelected()
    }

    /**
     * Properties
     */

    private lateinit var pictureButton: Button
    private lateinit var galleryButton: Button
    private lateinit var cancelButton: Button

    /**
     * View Life Cycle
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_picture_selection_dialog, container, false)
        setupView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    /**
     * Setup
     */

    private fun setupView(view: View) {
        pictureButton = view.findViewById(R.id.picture_button)
        galleryButton = view.findViewById(R.id.gallery_button)
        cancelButton = view.findViewById(R.id.cancel_button)
    }

    private fun setupListeners() {
        pictureButton.setOnClickListener {
            (targetFragment as Callbacks).onPictureSelected()
            dismiss()
        }

        galleryButton.setOnClickListener {
            (targetFragment as Callbacks).onGallerySelected()
            dismiss()
        }

        cancelButton.setOnClickListener { dismiss() }
    }

    /**
     * Initialize
     */

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment PictureSelectionDialogFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            PictureSelectionDialogFragment()
    }
}