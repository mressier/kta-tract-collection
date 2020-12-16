package com.unicorpdev.ktatract.shared.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unicorpdev.ktatract.R
import kotlinx.android.synthetic.main.dialog_bottom_pictures_add.*

class PicturesAddBottomDialogFragment: BottomSheetDialogFragment() {

    interface Callbacks {
        fun onPicturesSelected(pictures: List<String>)
    }

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private var callbacks: Callbacks? = null

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
        setupListeners()
    }

    override fun onAttach(context: Context) {
        callbacks = context as? Callbacks ?: parentFragment as? Callbacks
        super.onAttach(context)
    }

    override fun onDetach() {
        callbacks = null
        super.onDetach()
    }

    /***********************************************************************************************
     * Setup
     **********************************************************************************************/

    private fun setupListeners() {
        galleryTextView.setOnClickListener {

        }

        pictureTextView.setOnClickListener {

        }
    }
}