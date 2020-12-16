package com.unicorpdev.ktatract.fragments.fab

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.unicorpdev.ktatract.database.TractRepository
import com.unicorpdev.ktatract.models.TractPicture
import java.io.File
import java.util.*

class FabImageMenuViewModel: ViewModel() {

    enum class PictureSelectionOption {
        CREATE_ONE_TRACT,
        CREATE_MULTIPLE_TRACTS
    }

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    /** Database **/
    private val repository: TractRepository by lazy { TractRepository.get() }

    /** Parameters **/
    var shouldShowMultipleImport = true
    var isMenuVisible = false
    var tractId: UUID? = null

    lateinit var pictureSelectionOption: PictureSelectionOption

    /***********************************************************************************************
     * Methods
     **********************************************************************************************/

    fun generateTract(): UUID {
        val tract = repository.addEmptyTract()
        this.tractId = tract
        return tract
    }

    fun savePicturesFile(files: List<Uri>) {
        val tractId = this.tractId ?: repository.addEmptyTract()
        this.tractId = tractId

        files.forEach { file ->
            file.lastPathSegment?.let { filename ->
                Log.d("Pouet", "Filename: $filename")

                val tractPicture = TractPicture(
                    tractId = tractId,
                    photoFilename = filename
                )

                repository.addPicture(tractPicture)
            }
        }
    }

//    fun savePictureFile(): TractPicture? {
//        return pictureFile?.let { file ->
//
//            val tractId = this.tractId ?: repository.addEmptyTract()
//            this.tractId = tractId
//
//            val tractPicture = TractPicture(
//                tractId = tractId,
//                photoFilename = file.name
//            )
//
//            repository.addPicture(tractPicture)
//            tractPicture
//        }
//    }
}