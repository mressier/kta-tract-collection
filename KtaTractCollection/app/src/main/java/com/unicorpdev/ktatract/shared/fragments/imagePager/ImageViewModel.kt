package com.unicorpdev.ktatract.shared.fragments.imagePager

import androidx.lifecycle.ViewModel
import com.unicorpdev.ktatract.database.TractRepository
import java.io.File

class ImageViewModel: ViewModel() {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private val repository: TractRepository by lazy { TractRepository.get() }

    var imagePath: String? = null

    /***********************************************************************************************
     * Methods
     **********************************************************************************************/

    fun getPictureFile(): File? {
        return imagePath?.let { repository.getPictureFile(it) }
    }
}