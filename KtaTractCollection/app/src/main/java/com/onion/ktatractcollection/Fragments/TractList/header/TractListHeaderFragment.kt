package com.onion.ktatractcollection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class TractListHeaderFragment : Fragment() {

    /**
     * View Life Cycle
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tract_list_header, container, false)
    }

    /**
     * Companion
     */
    companion object {
        @JvmStatic
        fun newInstance() = TractListHeaderFragment()
    }
}