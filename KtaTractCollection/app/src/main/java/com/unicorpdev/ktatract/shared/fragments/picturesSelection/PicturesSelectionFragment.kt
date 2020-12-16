package com.unicorpdev.ktatract.shared.fragments.picturesSelection

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unicorpdev.ktatract.R


class PicturesSelectionFragment: PicturesSelectionable() {

    interface Callbacks {
        fun onPicturesSelected(pictures: List<Uri>)
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
        return inflater.inflate(R.layout.fragment_pictures_selection, container, false)
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
     * Callbacks
     **********************************************************************************************/

    override fun onPictureSelected(pictures: List<Uri>) {
        callbacks?.onPicturesSelected(pictures)
    }

}