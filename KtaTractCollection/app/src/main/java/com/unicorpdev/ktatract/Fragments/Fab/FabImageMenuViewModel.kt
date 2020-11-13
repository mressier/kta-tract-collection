package com.unicorpdev.ktatract.Fragments.Fab

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.unicorpdev.ktatract.Database.TractRepository
import com.unicorpdev.ktatract.Models.TractPicture
import java.io.File
import java.util.*

class FabImageMenuViewModel: ViewModel() {

    /**
     * Properties
     */

    var shouldShowMultipleImport = true

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

    fun generateTract(): UUID {
        val tract = repository.addEmptyTract()
        this.tractId = tract
        return tract
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
}