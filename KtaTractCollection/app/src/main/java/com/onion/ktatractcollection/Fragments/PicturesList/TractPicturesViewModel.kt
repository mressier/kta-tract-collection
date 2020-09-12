package com.onion.ktatractcollection.Fragments.PicturesList

import android.net.Uri
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

    fun convertPicturesToPath(pictures: List<TractPicture>): List<String> {
        return pictures.map { convertPictureToPath(it) }
    }

    fun convertPictureToPath(picture: TractPicture): String {
        return if (picture.isFromDevice) { picture.photoFilename }
        else {
            val file = convertPictureToFile(picture)
            file.path
        }
    }

    fun convertPictureToFile(picture: TractPicture): File =
        tractRepository.getPictureFile(picture.photoFilename)

    fun convertPictureToUri(picture: TractPicture): Uri {
        return Uri.parse(picture.photoFilename)
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