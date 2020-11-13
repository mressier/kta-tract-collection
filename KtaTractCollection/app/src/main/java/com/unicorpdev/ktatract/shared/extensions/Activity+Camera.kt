package com.unicorpdev.ktatract.shared.extensions

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.BasePermissionListener
import com.karumi.dexter.listener.single.CompositePermissionListener
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.shared.tools.isIntentAvailable
import java.io.File

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

    for (info in cameraActivities) {
        grantCameraPermission(info, photoUri)
    }
}

fun Activity.buildCameraIntent(photoUri: Uri): Intent? {
    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
    cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    grantCameraPermissionsForIntent(cameraIntent, photoUri)

    return if (isIntentAvailable(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY)) {
        cameraIntent
    } else {
        Toast.makeText(this, R.string.no_camera_error, Toast.LENGTH_SHORT).show()
        null
    }
}

fun Activity.buildCameraIntentForFile(file: File): Intent? {
    val uri = FileProvider.getUriForFile(
        this,
        getString(R.string.app_fileprovider_authority),
        file
    )

    return buildCameraIntent(uri)
}

fun Fragment.checkCameraPermission(onPermissionGranted: () -> Unit) {
    val snackbarPermissionListener = SnackbarOnDeniedPermissionListener.Builder
        .with(view, R.string.camera_permission_description)
        .withOpenSettingsButton(R.string.settings)
        .withCallback(object : Snackbar.Callback() {
            override fun onShown(snackbar: Snackbar) {}
            override fun onDismissed(snackbar: Snackbar, event: Int) {}
        }).build()

    val grantedPermissionListener = object : BasePermissionListener() {
        override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
            onPermissionGranted()
        }
    }

    val compositePermissionListener = CompositePermissionListener(
        snackbarPermissionListener,
        grantedPermissionListener
    )

    Dexter.withContext(requireContext())
        .withPermission(Manifest.permission.CAMERA)
        .withListener(compositePermissionListener)
        .check()
}