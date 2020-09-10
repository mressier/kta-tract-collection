package com.onion.ktatractcollection.shared.tools

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.provider.MediaStore

fun Activity.grantCameraPermission(info: ResolveInfo, photoUri: Uri) {
    grantUriPermission(
        info.activityInfo.packageName,
        photoUri,
        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
    )
}

fun Activity.grantCameraPermissionsForIntent(intent: Intent, photoUri: Uri) {
    val cameraActivities =
        packageManager.queryIntentActivities(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY
        )

    println(cameraActivities)
    for (info in cameraActivities) {
        grantCameraPermission(info, photoUri)
    }
}

fun Activity.buildCameraIntent(photoUri: Uri): Intent {
    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
    cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    grantCameraPermissionsForIntent(cameraIntent, photoUri)
    return cameraIntent
}