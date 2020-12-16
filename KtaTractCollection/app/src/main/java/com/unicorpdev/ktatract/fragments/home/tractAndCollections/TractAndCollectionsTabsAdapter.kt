package com.unicorpdev.ktatract.fragments.home.tractAndCollections

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.fragments.collections.AllTractCollectionsFragment
import com.unicorpdev.ktatract.fragments.tractList.AllTractsFragment

class TractAndCollectionsTabsAdapter(
    fragment: Fragment,
    private val items: Array<TabContent> = TabContent.values()
    ): FragmentStateAdapter(fragment) {


    enum class TabContent(@StringRes val title: Int) {
        ALL_TRACTS(R.string.tracts_tab),
        COLLECTIONS(R.string.collections_tab)
    }


    override fun getItemCount(): Int {
        return items.count()
    }

    override fun createFragment(position: Int): Fragment {
        return when (items[position]) {
            TabContent.ALL_TRACTS -> AllTractsFragment.newInstance()
            TabContent.COLLECTIONS -> AllTractCollectionsFragment.newInstance()
        }
    }
}