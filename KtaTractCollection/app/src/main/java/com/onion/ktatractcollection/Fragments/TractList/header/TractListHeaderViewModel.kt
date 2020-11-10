package com.onion.ktatractcollection.Fragments.TractList.header

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.onion.ktatractcollection.Database.TractRepository
import com.onion.ktatractcollection.Fragments.TractList.dialogs.TractListParameters
import com.onion.ktatractcollection.Models.Tract

class TractListHeaderViewModel : ViewModel() {

    /** Display list mode saved for button aspect **/
    var displayMode: TractListParameters.DisplayMode = TractListParameters.DisplayMode.LIST
}