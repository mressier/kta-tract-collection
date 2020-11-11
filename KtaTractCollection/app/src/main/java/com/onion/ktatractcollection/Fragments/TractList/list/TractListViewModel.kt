package com.onion.ktatractcollection.Fragments.TractList.list

import com.onion.ktatractcollection.Fragments.TractList.TractWithPicture
import com.onion.ktatractcollection.Fragments.TractList.parameters.DisplayMode
import com.onion.ktatractcollection.Fragments.TractList.parameters.TractListParameters
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.Models.TractPicture
import com.onion.ktatractcollection.shared.extensions.mergeWithPictures
import com.onion.ktatractcollection.shared.extensions.sortAndFilter
import com.onion.ktatractcollection.shared.viewmodel.RepositoryViewModel

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

