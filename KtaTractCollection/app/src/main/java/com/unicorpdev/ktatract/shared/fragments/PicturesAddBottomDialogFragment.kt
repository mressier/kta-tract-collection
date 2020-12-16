package com.unicorpdev.ktatract.shared.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.shared.fragments.picturesSelection.PictureSelectionMethod
import com.unicorpdev.ktatract.shared.fragments.picturesSelection.PicturesSelectionFragment
import com.unicorpdev.ktatract.shared.fragments.picturesSelection.PicturesSelectionable
import kotlinx.android.synthetic.main.dialog_bottom_pictures_add.*

class PicturesAddBottomDialogFragment:
    BottomSheetDialogFragment(),
    PicturesSelectionFragment.Callbacks
{

    interface Callbacks {
        fun onPicturesSelected(pictures: List<Uri>)
    }

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    var callbacks: Callbacks? = null

    private lateinit var picturesSelectionFragment: PicturesSelectionable

    /***********************************************************************************************
     * View Life Cycle
     **********************************************************************************************/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_bottom_pictures_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragments()
        setupListeners()
    }

    override fun onAttach(context: Context) {
        callbacks = callbacks ?: context as? Callbacks ?: parentFragment as? Callbacks
        super.onAttach(context)
    }

    override fun onDetach() {
        callbacks = null
        super.onDetach()
    }


    /***********************************************************************************************
     * Setup
     **********************************************************************************************/

    private fun setupFragments() {
        picturesSelectionFragment = childFragmentManager
                .findFragmentById(R.id.pictureSelectionFragment) as PicturesSelectionFragment
    }

    private fun setupListeners() {
        galleryTextView.setOnClickListener {
            picturesSelectionFragment.startPictureSelection(
                PictureSelectionMethod.GALLERY,
                false
            )
        }

        pictureTextView.setOnClickListener {
            picturesSelectionFragment.startPictureSelection(PictureSelectionMethod.CAMERA)
        }
    }

    /***********************************************************************************************
     * Callbacks
     **********************************************************************************************/

    override fun onPicturesSelected(pictures: List<Uri>) {
        Log.d(TAG, "GET PICTURES : $pictures")
        callbacks?.onPicturesSelected(pictures)
    }

    companion object {
        val TAG = PicturesAddBottomDialogFragment::class.simpleName ?: "Default"

        fun newInstance() = PicturesAddBottomDialogFragment()
    }
}