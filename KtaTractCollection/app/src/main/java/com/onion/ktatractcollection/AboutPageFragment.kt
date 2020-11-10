package com.onion.ktatractcollection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * A simple [Fragment] subclass.
 * Use the [AboutPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AboutPageFragment : Fragment() {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/



    /***********************************************************************************************
     * View Life Cycle
     **********************************************************************************************/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_page, container, false)
    }


    companion object {
        @JvmStatic
        fun newInstance() = AboutPageFragment()
    }
}