package com.unicorpdev.ktatract.shared.extensions

import android.view.View

fun View.setIsVisible(isVisible: Boolean, animated: Boolean = true) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE

    if (!animated) { this.jumpDrawablesToCurrentState() }
}