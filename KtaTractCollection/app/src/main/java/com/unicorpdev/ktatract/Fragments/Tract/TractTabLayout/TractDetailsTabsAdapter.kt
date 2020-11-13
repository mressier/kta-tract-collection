package com.unicorpdev.ktatract.Fragments.Tract.TractTabLayout

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.unicorpdev.ktatract.Fragments.PicturesList.TractPicturesFragment
import java.util.*

class TractDetailsTabsAdapter(fragment: Fragment, val itemsCount: Int, var tractId: UUID?): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TractPicturesFragment.newInstance(tractId)
            else -> Fragment()
        }
    }
}