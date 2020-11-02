package com.onion.ktatractcollection.shared.fragments.imagePager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.onion.ktatractcollection.R
import kotlinx.android.synthetic.main.fragment_image_pager.*

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
        return inflater.inflate(R.layout.fragment_image_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupTabLayout()
        updateUI()
    }

    /**
     * Setup
     */
    private fun setupViewPager() {
        viewPager.adapter = ImagePagerAdapter(this, imagePathArray)
        viewPager.currentItem = currentIndex

        if (imagePathArray.isEmpty() || imagePathArray.size == 1) {
            tabLayout.visibility = View.GONE
        }
    }

    private fun setupTabLayout() {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            Log.d(TAG, "tab: $tab, position: $position")
        }.attach()
    }

    private fun updateUI() {}

    /**
     * Static methods
     */
    companion object {
        private const val TAG = "ImagePagerFragment"
    }
}