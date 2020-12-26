package com.unicorpdev.ktatract.utils

import com.unicorpdev.ktatract.models.Tract
import java.util.*

fun newTract(): Tract {
    return Tract(collectionId = UUID.randomUUID())
}