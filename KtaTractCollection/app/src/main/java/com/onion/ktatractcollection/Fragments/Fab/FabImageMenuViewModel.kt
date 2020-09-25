package com.onion.ktatractcollection.Fragments.Fab

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.onion.ktatractcollection.Database.TractRepository
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.Models.TractPicture
import java.io.File

class FabImageMenuViewModel: ViewModel() {

    /**
     * Properties
     */

    var isMenuVisible = false

    var pictureFile: File? = null

    private val repository: TractRepository by lazy { TractRepository.get() }

    /**
     * Methods
     */

    fun generatePictureFile(): File {
        val file = repository.getPictureFile(TractPicture.randomFilename)
        pictureFile = file
        return file
    }

    fun savePictureFile(): TractPicture? {
        return pictureFile?.let { file ->
            val tract = Tract()
            repository.addTract(tract)

            val tractPicture = TractPicture(
                tractId = tract.id,
                isFromDevice = false,
                photoFilename = file.toUri().toString()
            )
            repository.addPicture(tractPicture)
            tractPicture
        }
    }

    fun savePictures(uri: Array<Uri>): Array<TractPicture> {
        val tract = Tract()
        repository.addTract(tract)

        val pictures = uri.map {
            TractPicture(tractId = tract.id,
                isFromDevice = false,
                photoFilename = it.toString())
        }
        pictures.forEach { repository.addPicture(it) }

        return pictures.toTypedArray()
    }
}