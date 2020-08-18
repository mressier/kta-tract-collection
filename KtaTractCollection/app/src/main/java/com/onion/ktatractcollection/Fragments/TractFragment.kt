package com.onion.ktatractcollection.Fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.onion.ktatractcollection.R

class TractFragment : Fragment() {

    companion object {
        fun newInstance() = TractFragment()
    }

    private lateinit var viewModel: TractViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tract_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TractViewModel::class.java)
        // TODO: Use the ViewModel
    }

}