package com.unicorpdev.ktatract.fragments.tractList.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.unicorpdev.ktatract.fragments.tractList.parameters.DisplayMode
import com.unicorpdev.ktatract.fragments.tractList.parameters.TractListParameters
import com.unicorpdev.ktatract.models.Tract
import com.unicorpdev.ktatract.models.TractPicture
import com.unicorpdev.ktatract.models.TractWithPicture
import com.unicorpdev.ktatract.shared.extensions.mergeWithPictures
import com.unicorpdev.ktatract.shared.extensions.sortAndFilter
import com.unicorpdev.ktatract.shared.viewmodel.RepositoryViewModel
import java.util.*

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

    /** Collection **/

    private var collectionId = MutableLiveData<UUID?>().apply { value = null }

    // Item to observe
    var tractList: LiveData<List<Tract>> = Transformations.switchMap(collectionId) { id ->
        if (id != null) {
            tractRepository.getTractsForCollectionLiveData(id)
        } else {
            tractRepository.getTractsLiveData()
        }
    }

    var picturesList: LiveData<List<TractPicture>> = Transformations.switchMap(collectionId) { id ->
        if (id != null) {
            tractRepository.getPicturesForCollectionLiveData(id)
        } else {
            tractRepository.getPicturesLiveData()
        }
    }

    /***********************************************************************************************
     * Methods
     **********************************************************************************************/

    // fun to call to update collection information
    fun loadCollection(id: UUID?) {
        collectionId.value = id
    }

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

