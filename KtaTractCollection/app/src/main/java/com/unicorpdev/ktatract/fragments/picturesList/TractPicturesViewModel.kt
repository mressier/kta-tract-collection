package com.unicorpdev.ktatract.fragments.picturesList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.unicorpdev.ktatract.database.TractRepository
import com.unicorpdev.ktatract.models.Tract
import com.unicorpdev.ktatract.models.TractPicture
import com.unicorpdev.ktatract.models.TractWithPicture
import com.unicorpdev.ktatract.shared.extensions.mergeWithPictures
import com.unicorpdev.ktatract.shared.extensions.sortAndFilter
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

    var tract: LiveData<Tract> = Transformations.switchMap(tractId) { id ->
        tractRepository.getTractLiveData(id)
    }

    // Saved items

    var savedTract: Tract = Tract(collectionId = tractRepository.defaultCollectionId)
    var savedPictures: List<TractPicture> = listOf()

    val savedPicturesFile: List<File>
        get() = savedPictures.map { tractRepository.getPictureFile(it.photoFilename) }

    /** Computed **/

    val savedTractWithPictures: TractWithPicture
        get() {
            val tracts = listOf(savedTract).mergeWithPictures(savedPictures)
            return addPicturesFile(tracts).first()
        }
    // Warning: duplicated
    private fun addPicturesFile(tractsWithPicture: List<TractWithPicture>): List<TractWithPicture> {
        return tractsWithPicture.map { tractWithPicture ->
            tractWithPicture.apply {
                picturesFile = pictures.map {
                    it.id to tractRepository.getPictureFile(it.photoFilename)
                }.toMap()
            }
        }
    }

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