package com.unicorpdev.ktatract.fragments.TractList.header

import androidx.lifecycle.ViewModel
import com.unicorpdev.ktatract.fragments.TractList.parameters.DisplayMode

class TractListHeaderViewModel : ViewModel() {

    /** Display list mode saved for button aspect **/
    var displayMode: DisplayMode = DisplayMode.LIST
}