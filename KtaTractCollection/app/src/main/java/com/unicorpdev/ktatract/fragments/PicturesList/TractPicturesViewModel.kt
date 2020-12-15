package com.unicorpdev.ktatract.fragments.PicturesList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.unicorpdev.ktatract.database.TractRepository
import com.unicorpdev.ktatract.models.TractPicture
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
        tractRepository.getPicturesLiveData(id)
    }

    var savedPictures: List<TractPicture> = listOf()

    val savedPicturesFile: List<File>
        get() = savedPictures.map { tractRepository.getPictureFile(it.photoFilename) }

    /**
     * Methods
     */

    fun loadPicturesForTractId(id: UUID) {
        tractId.value = id
    }

    fun deletePicture(picture: TractPicture) {
        tractRepository.deletePicture(picture)
    }
}