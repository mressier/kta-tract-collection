package com.unicorpdev.ktatract.Fragments.PicturesList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.unicorpdev.ktatract.Database.TractRepository
import com.unicorpdev.ktatract.Models.TractPicture
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

    fun getPicturesPath(pictures: List<TractPicture>): List<String> {
        return pictures.map { it.photoFilename }
    }

    fun loadPicturesForTractId(id: UUID) {
        tractId.value = id
    }

    fun deletePicture(picture: TractPicture) {
        tractRepository.deletePicture(picture)
    }
}