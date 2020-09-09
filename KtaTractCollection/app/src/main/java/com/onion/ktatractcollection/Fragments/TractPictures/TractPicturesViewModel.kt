package com.onion.ktatractcollection.Fragments.TractPictures

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.onion.ktatractcollection.Database.TractRepository
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.Models.TractPicture
import java.io.File
import java.util.*

class TractPicturesViewModel: ViewModel() {

    /**
     * Properties
     */

    private val tractRepository = TractRepository.get()

    /** One tract **/
    private var tractId = MutableLiveData<UUID>()

    // Item to observe
    var pictures: LiveData<List<TractPicture>> = Transformations.switchMap(tractId) { id ->
        tractRepository.getPictures(id)
    }

    /**
     * Methods
     */

    fun convertPicturesToFile(pictures: List<TractPicture>): List<File> {
        return tractRepository.getPicturesFile(pictures)
    }

    fun convertPictureToFile(picture: TractPicture): File {
        return tractRepository.getPictureFile(picture.photoFilename)
    }

    fun loadPicturesForTractId(id: UUID) {
        tractId.value = id
    }

    fun savePicture(picture: TractPicture) {
        tractRepository.addPicture(picture)
    }

    fun deletePicture(picture: TractPicture) {
        tractRepository.deletePicture(picture)
    }
}