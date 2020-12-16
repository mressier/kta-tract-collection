package com.unicorpdev.ktatract.shared.fragments

import android.util.Log
import androidx.lifecycle.ViewModel
import com.unicorpdev.ktatract.database.TractRepository
import com.unicorpdev.ktatract.models.TractPicture
import java.io.File

class PicturesSelectionableViewModel: ViewModel() {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/
    
    private val repository: TractRepository by lazy { TractRepository.get() }

    var pictureFile: File? = null

    /***********************************************************************************************
     * Methods
     **********************************************************************************************/
    
    fun generatePictureFile(): File {
        val file = repository.getPictureFile(TractPicture.randomFilename())
        pictureFile = file
        return file
    }
}