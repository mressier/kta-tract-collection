package com.unicorpdev.ktatract.fragments.collections

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unicorpdev.ktatract.R

/**
 * A simple [Fragment] subclass.
 * Use the [TractCollectionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TractCollectionsFragment : Fragment() {

    /***********************************************************************************************
     * View Life Cycle
     **********************************************************************************************/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_collections, container, false)
    }

    /***********************************************************************************************
     * Companion
     **********************************************************************************************/

    companion object {
        @JvmStatic
        fun newInstance() = TractCollectionsFragment()
    }
}