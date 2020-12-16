package com.unicorpdev.ktatract.shared.fragments.listHeader

import androidx.lifecycle.ViewModel
import com.unicorpdev.ktatract.fragments.tractList.parameters.DisplayMode

class ListHeaderViewModel : ViewModel() {

    /** Display list mode saved for button aspect **/
    var displayMode: DisplayMode = DisplayMode.LIST
}