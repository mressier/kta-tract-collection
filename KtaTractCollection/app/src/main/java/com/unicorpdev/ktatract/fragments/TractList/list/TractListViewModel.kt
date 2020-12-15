package com.unicorpdev.ktatract.fragments.TractList.list

import com.unicorpdev.ktatract.fragments.TractList.parameters.DisplayMode
import com.unicorpdev.ktatract.fragments.TractList.parameters.TractListParameters
import com.unicorpdev.ktatract.models.Tract
import com.unicorpdev.ktatract.models.TractPicture
import com.unicorpdev.ktatract.models.TractWithPicture
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
            val tracts = savedTracts.sortAndFilter(listParameters).mergeWithPictures(savedPictures)
            return addPicturesFile(tracts)
        }

    /***********************************************************************************************
     * Methods
     **********************************************************************************************/

    private fun addPicturesFile(tractsWithPicture: List<TractWithPicture>): List<TractWithPicture> {
        return tractsWithPicture.map { tractWithPicture ->
            tractWithPicture.apply {
                picturesFile = pictures.map {
                    it.id to tractRepository.getPictureFile(it.photoFilename)
                }.toMap()
            }
        }
    }
}

