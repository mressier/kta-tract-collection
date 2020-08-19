package com.onion.ktatractcollection.Fragments.TractList

import androidx.lifecycle.ViewModel
import com.onion.ktatractcollection.Fragments.TractList.dummy.DummyContent
import com.onion.ktatractcollection.Models.Tract

class TractListViewModel: ViewModel() {

    var tracts: List<Tract> = DummyContent.ITEMS
}