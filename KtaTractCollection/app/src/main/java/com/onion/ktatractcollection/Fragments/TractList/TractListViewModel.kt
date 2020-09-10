package com.onion.ktatractcollection.Fragments.TractList

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.onion.ktatractcollection.Database.TractRepository
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.Models.TractPicture
import java.io.File
import java.util.*

class TractWithPicture(val tract: Tract, var picturesFile: List<File> = listOf()) {}

class TractListViewModel: ViewModel() {

    /**
     * Properties
     */
    private val tractRepository = TractRepository.get()

    /**
     * Live Data
     */

    /** Tract list saved locally **/
    val tracts: LiveData<List<Tract>> = tractRepository.getTracts()

    var tractsWithPicture: List<TractWithPicture> = listOf()

    fun saveTract(tract: Tract) {
        tractRepository.addTract(tract)
    }

    fun deleteTract(tract: Tract) {
        getSavedTractWithPictures(tract.id)?.picturesFile?.let {
            tractRepository.deletePicturesFiles(it)
        }
        tractRepository.deleteTract(tract)

        tractsWithPicture = tractsWithPicture.filter { it.tract.id != tract.id }
    }

    /**
     * Pictures
     */

    fun saveAsTractsWithPicture(tracts: List<Tract>) {
        val newTractsWithPicture = tracts.map { tract ->
            val oldPicture = getSavedTractWithPictures(tract.id)?.picturesFile
            TractWithPicture(tract, oldPicture ?: listOf())
        }

        tractsWithPicture = newTractsWithPicture
    }

    fun addPicturesToTractItem(tractId: UUID, pictures: List<TractPicture>) {
        val picturesFile = pictures.map { convertPictureToFile(it) }
        tractsWithPicture.find { it.tract.id == tractId }?.picturesFile = picturesFile
    }

    fun getPictures(tractId: UUID): LiveData<List<TractPicture>> {
        return tractRepository.getPictures(tractId)
    }

    private fun convertPictureToFile(picture: TractPicture): File {
        return tractRepository.getPictureFile(picture.photoFilename)
    }

    private fun getSavedTractWithPictures(tractId: UUID): TractWithPicture? {
        return tractsWithPicture.find { it.tract.id == tractId }
    }

}