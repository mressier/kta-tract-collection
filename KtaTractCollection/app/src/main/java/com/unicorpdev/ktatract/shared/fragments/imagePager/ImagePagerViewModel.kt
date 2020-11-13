package com.unicorpdev.ktatract.shared.fragments.imagePager

import androidx.lifecycle.ViewModel
import com.unicorpdev.ktatract.Database.TractRepository
import java.io.File

class ImagePagerViewModel : ViewModel() {

    var imagePathArray: Array<String> = arrayOf()
    var currentIndex: Int = 0

}