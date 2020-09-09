package com.onion.ktatractcollection.Fragments.TractList

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.onion.ktatractcollection.Database.TractRepository
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.Models.TractPicture
import java.io.File

class TractWithPicture(val tract: Tract, var pictureFile: File? = null) {}

class TractListViewModel: ViewModel() {

    /**
     * Properties
     */
    private val tractRepository = TractRepository.get()

    /**
     * Live Data
     */

    /** Tract list saved locally **/
    var tracts = tractRepository.getTracts()

    var tractsWithPicture: List<TractWithPicture> = listOf()

    fun saveTract(tract: Tract) {
        tractRepository.addTract(tract)
    }

    fun deleteTract(tract: Tract) {
        tractRepository.deleteTract(tract)
    }

    fun saveAsTractsWithPicture(tracts: List<Tract>) {
        val oldTractWithPictures = tractsWithPicture

        tractsWithPicture = tracts.map { tract ->
            val oldPicture = oldTractWithPictures.find { it.tract.id == tract.id }?.pictureFile
            TractWithPicture(tract, oldPicture)
        }
    }

    fun addPictureToTractItem(tractIndex: Int, picture: TractPicture?) {
        if (tractIndex < 0 || tractIndex >= tractsWithPicture.size) { return }

        val pictureFile = picture?.let { convertPictureToFile(it) }
        tractsWithPicture[tractIndex].pictureFile = pictureFile
    }

    /**
     * Pictures
     */

    fun getPictures(tract: Tract): LiveData<List<TractPicture>> {
        return tractRepository.getPictures(tract.id)
    }

    private fun convertPictureToFile(picture: TractPicture): File {
        return tractRepository.getPictureFile(picture.photoFilename)
    }

}