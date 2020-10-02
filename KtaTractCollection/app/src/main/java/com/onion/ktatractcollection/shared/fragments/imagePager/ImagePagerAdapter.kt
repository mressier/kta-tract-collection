package com.onion.ktatractcollection.shared.fragments.imagePager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ImagePagerAdapter(fragment: Fragment, val items: Array<String>): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun createFragment(position: Int): Fragment {
        return ImagePreviewFragment.newInstance(items[position])
    }
}