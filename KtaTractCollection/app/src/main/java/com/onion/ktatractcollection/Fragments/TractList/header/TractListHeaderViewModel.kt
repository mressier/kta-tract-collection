package com.onion.ktatractcollection.Fragments.TractList.header

import androidx.lifecycle.ViewModel
import com.onion.ktatractcollection.Fragments.TractList.parameters.DisplayMode

class TractListHeaderViewModel : ViewModel() {

    /** Display list mode saved for button aspect **/
    var displayMode: DisplayMode = DisplayMode.LIST
}