package com.unicorpdev.ktatract.fragments.tractList.header

import androidx.lifecycle.ViewModel
import com.unicorpdev.ktatract.fragments.tractList.parameters.DisplayMode

class TractListHeaderViewModel : ViewModel() {

    /** Display list mode saved for button aspect **/
    var displayMode: DisplayMode = DisplayMode.LIST
}