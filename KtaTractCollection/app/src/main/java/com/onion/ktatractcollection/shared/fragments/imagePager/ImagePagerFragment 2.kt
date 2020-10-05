package com.onion.ktatractcollection.shared.fragments.imagePager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.onion.ktatractcollection.R

/**
 * A simple [Fragment] subclass.
 * Use the [ImagePagerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImagePagerFragment : Fragment() {

    /**
     * Properties
     */

    /* Parameters */
    private var imagePathArray: Array<String> = arrayOf()
    private var currentIndex: Int = 0

    /* Outlets */
    private lateinit var viewPager: ViewPager2

    /**
     * View Life Cycle
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val args = ImagePagerFragmentArgs.fromBundle(it)
            this.imagePathArray = args.pathArray
            this.currentIndex = args.currentIndex
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_image_pager, container, false)
        setupView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
    }

    /**
     * Setup
     */
    private fun setupView(view: View) {
        viewPager = view.findViewById(R.id.view_pager)
        viewPager.adapter = ImagePagerAdapter(this, imagePathArray)
        viewPager.currentItem = currentIndex
    }

    private fun updateUI() {}

    /**
     * Static methods
     */
    companion object {}
}