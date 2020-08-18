package com.onion.ktatractcollection.Shared.Tools

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager

fun Activity.isIntentAvailable(intent: Intent, flags: Int): Boolean {
    val resolvedActivity = packageManager.resolveActivity(intent, flags)
    return resolvedActivity != null
}