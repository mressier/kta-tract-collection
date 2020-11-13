package com.unicorpdev.ktatract.Fragments.TractList.list

import com.unicorpdev.ktatract.Fragments.TractList.TractWithPicture
import com.unicorpdev.ktatract.Fragments.TractList.parameters.DisplayMode
import com.unicorpdev.ktatract.Fragments.TractList.parameters.TractListParameters
import com.unicorpdev.ktatract.Models.Tract
import com.unicorpdev.ktatract.Models.TractPicture
import com.unicorpdev.ktatract.shared.extensions.mergeWithPictures
import com.unicorpdev.ktatract.shared.extensions.sortAndFilter
import com.unicorpdev.ktatract.shared.viewmodel.RepositoryViewModel

class TractListViewModel: RepositoryViewModel() {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    /** Saved repository data **/

    var savedTracts: List<Tract> = listOf()
    var savedPictures: List<TractPicture> = listOf()

    /** Parameters **/

    var listParameters: TractListParameters = TractListParameters()
    var displayMode: DisplayMode = DisplayMode.LIST

    /** Computed **/

    val savedTractWithPictures: List<TractWithPicture>
        get() {
            return savedTracts.sortAndFilter(listParameters).mergeWithPictures(savedPictures)
        }

    /***********************************************************************************************
     * Methods
     **********************************************************************************************/
}

