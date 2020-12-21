package com.unicorpdev.ktatract

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * A simple [Fragment] subclass.
 * Use the [CollectionSpinnerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CollectionSpinnerFragment : Fragment() {

    /***********************************************************************************************
     * View Life Cycle
     **********************************************************************************************/
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collection_spinner, container, false)
    }

    /***********************************************************************************************
     * Companion
     **********************************************************************************************/
    
    companion object {
        
        @JvmStatic
        fun newInstance() = CollectionSpinnerFragment()
    }
}