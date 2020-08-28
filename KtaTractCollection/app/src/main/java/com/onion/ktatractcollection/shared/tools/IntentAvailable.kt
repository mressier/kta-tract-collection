package com.onion.ktatractcollection.shared.tools

import android.app.Activity
import android.content.Intent

fun Activity.isIntentAvailable(intent: Intent, flags: Int): Boolean {
    val resolvedActivity = packageManager.resolveActivity(intent, flags)
    return resolvedActivity != null
}