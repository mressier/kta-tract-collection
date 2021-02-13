package com.unicorpdev.ktatract.shared.fragments.imagePager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.shared.fragments.imagePager.ImagePagerFragmentArgs
import kotlinx.android.synthetic.main.fragment_collection_header.*
import kotlinx.android.synthetic.main.fragment_image_pager.*

/**
 * A simple [Fragment] subclass.
 * Use the [ImagePagerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImagePagerFragment : Fragment() {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private val viewModel: ImagePagerViewModel by lazy {
        ViewModelProvider(this).get(ImagePagerViewModel::class.java)
    }

    /***********************************************************************************************
     * View Life Cycle
     **********************************************************************************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val args = ImagePagerFragmentArgs.fromBundle(it)
            viewModel.imagePathArray = args.pathArray
            viewModel.currentIndex = args.currentIndex
            viewModel.title = args.title ?: ""
            viewModel.description = args.description ?: ""
            viewModel.additionalDescription = args.additionnalDescription ?: ""
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

    /***********************************************************************************************
     * Setup
     **********************************************************************************************/

    private fun setupViewPager() {
        viewPager.adapter = ImagePagerAdapter(this, viewModel.imagePathArray)
        Log.d(TAG, "current index ${viewModel.currentIndex}")

        if (viewModel.imagePathArray.size <= 1) {
            tabLayout.visibility = View.GONE
        }
    }

    private fun setupTabLayout() {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            Log.d(TAG, "tab: $tab, position: $position")
        }.attach()
    }

    private fun updateUI() {
        viewPager.currentItem = viewModel.currentIndex
        requireActivity().title =
            if (viewModel.title.isBlank()) getString(R.string.unknown) else viewModel.title
        tractDatationTextView.text = viewModel.additionalDescription
        tractDescriptionTextView.text = viewModel.description
        Log.d(TAG, "get title: ${viewModel.title}")
    }

    /***********************************************************************************************
     * Companion
     **********************************************************************************************/

    companion object {
        private val TAG = ImagePagerFragment::class.simpleName ?: "Default"
    }
}