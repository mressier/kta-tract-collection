package com.onion.ktatractcollection.Fragments.Fab

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.onion.ktatractcollection.Database.TractRepository
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.Models.TractPicture
import java.io.File
import java.util.*

class FabImageMenuViewModel: ViewModel() {

    /**
     * Properties
     */

    var isMenuVisible = false

    var pictureFile: File? = null

    var tractId: UUID? = null

    private val repository: TractRepository by lazy { TractRepository.get() }

    /**
     * Methods
     */

    fun generatePictureFile(): File {
        val file = repository.getPictureFile(TractPicture.randomFilename())
        pictureFile = file
        return file
    }

    fun savePictureFile(): TractPicture? {
        return pictureFile?.let { file ->

            val tractId = this.tractId ?: repository.addEmptyTract()
            this.tractId = tractId

            val tractPicture = TractPicture(
                tractId = tractId,
                photoFilename = file.toUri().toString()
            )

            repository.addPicture(tractPicture)
            tractPicture
        }
    }

    fun savePictures(uri: Array<Uri>): Array<TractPicture> {
        val tractId = this.tractId ?: repository.addEmptyTract()
        this.tractId = tractId

        val pictures = uri.map {
            TractPicture(
                tractId = tractId,
                photoFilename = it.toString()
            )
        }

        pictures.forEach { repository.addPicture(it) }

        return pictures.toTypedArray()
    }
}