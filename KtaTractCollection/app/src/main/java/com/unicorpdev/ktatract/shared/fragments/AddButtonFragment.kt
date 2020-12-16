package com.unicorpdev.ktatract.shared.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unicorpdev.ktatract.R
import kotlinx.android.synthetic.main.fragment_add_button.*

/**
 * A simple [Fragment] subclass.
 * Use the [AddButtonFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddButtonFragment : Fragment() {

    interface Callbacks {
        fun onAddButtonSelected()
    }

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private var callbacks: Callbacks? = null

    /***********************************************************************************************
     * View Life Cycle
     **********************************************************************************************/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_button, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
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

    private fun setupView() {}

    private fun setupListeners() {
        imageButtonAdd.setOnClickListener {
            callbacks?.onAddButtonSelected()
        }

        view?.setOnClickListener {
            callbacks?.onAddButtonSelected()
        }
    }

    /***********************************************************************************************
     * COmpanion
     **********************************************************************************************/

    companion object {

        @JvmStatic
        fun newInstance() = AddButtonFragment()
    }
}