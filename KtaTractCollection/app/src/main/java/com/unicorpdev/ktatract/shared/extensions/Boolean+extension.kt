package com.unicorpdev.ktatract.shared.extensions

fun Boolean.toInt(): Int {
    return if (this) { 1 } else { 0 }
}