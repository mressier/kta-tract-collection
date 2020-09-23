package com.onion.ktatractcollection.Fragments.PicturesList

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.onion.ktatractcollection.R

/**
 * A [DialogFragment] giving the choice between take a picture and choose a picture from gallery
 *
 * Create an instance with PictureSelectionDialogFragment.newInstance()
 */
class PictureSelectionDialogFragment : DialogFragment() {

    interface Callbacks {
        fun onCameraOptionSelected()
        fun onGalleryOptionSelected()
    }

    /**
     * Properties
     */

    private lateinit var pictureButton: Button
    private lateinit var galleryButton: Button

    /**
     * View Life Cycle
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = activity ?: throw IllegalStateException("Activity cannot be null")
        val inflater = activity.layoutInflater

        val view = inflater.inflate(R.layout.fragment_picture_selection_dialog, null)
        setupView(view)
        setupListeners()

        val builder = AlertDialog.Builder(activity)
        builder.setView(view)
        builder.setCancelable(true)
        builder.setNegativeButton(android.R.string.cancel) { _, _ -> dismiss() }
        return builder.create()
    }

    /**
     * Setup
     */

    private fun setupView(view: View) {
        pictureButton = view.findViewById(R.id.picture_button)
        galleryButton = view.findViewById(R.id.gallery_button)
    }

    private fun setupListeners() {
        pictureButton.setOnClickListener {
            (targetFragment as Callbacks).onCameraOptionSelected()
            dismiss()
        }

        galleryButton.setOnClickListener {
            (targetFragment as Callbacks).onGalleryOptionSelected()
            dismiss()
        }
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
        @JvmStatic
        fun newInstance() = PictureSelectionDialogFragment()
    }
}