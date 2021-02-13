package com.unicorpdev.ktatract.shared.fragments.imagePager

import androidx.lifecycle.ViewModel

class ImagePagerViewModel : ViewModel() {

    var imagePathArray: Array<String> = arrayOf()
    var currentIndex: Int = 0
    var title: String = ""
    var description: String = ""
    var additionalDescription: String = ""

}