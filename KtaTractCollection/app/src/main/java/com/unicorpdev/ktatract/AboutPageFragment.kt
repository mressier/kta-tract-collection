package com.unicorpdev.ktatract

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.unicorpdev.ktatract.R
import kotlinx.android.synthetic.main.fragment_about_page.*


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    /***********************************************************************************************
     * Methods
     **********************************************************************************************/

    private fun sendEmail() {
        val intent = Intent(Intent.ACTION_SEND) //common intent
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, "About KtaTract")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("mathilderessier@hotmail.fr"));

        startActivity(intent)
    }

    /***********************************************************************************************
     * Setup
     **********************************************************************************************/

    private fun setupListeners() {
        button.setOnClickListener { sendEmail() }
    }

    /***********************************************************************************************
     * Companion
     **********************************************************************************************/

    companion object {
        @JvmStatic
        fun newInstance() = AboutPageFragment()
    }
}