package com.unicorpdev.ktatract.Fragments.TractList.header

import androidx.lifecycle.ViewModel
import com.unicorpdev.ktatract.Fragments.TractList.parameters.DisplayMode

class TractListHeaderViewModel : ViewModel() {

    /** Display list mode saved for button aspect **/
    var displayMode: DisplayMode = DisplayMode.LIST
}