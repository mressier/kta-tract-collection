package com.unicorpdev.ktatract.models

import java.io.File
import java.util.*

class TractWithPicture(
    val tract: Tract,
    var pictures: List<TractPicture> = listOf(),
    var picturesFile: Map<UUID, File> = mapOf()
) {}
