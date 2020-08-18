package com.onion.ktatractcollection.Shared.Tools

import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.fragment.app.Fragment
import com.onion.ktatractcollection.R

/**
 * Granted permissions
 */

fun Fragment.permissionHasBeenGranted(
    permission: String,
    permissions: Array<out String>,
    grantResults: IntArray
): Boolean {
    val permissionIndex = permissions.indexOf(permission)

    if (grantResults.size < 0 || permissionIndex >= grantResults.size) {
        Log.d("TAG", "Grant result size or permission is invalid")
        return false
    }

    val result = grantResults[permissionIndex]
    return result == PackageManager.PERMISSION_GRANTED
}

fun Context.permissionIsGranted(permission: String): Boolean {
    val userPermission = checkSelfPermission(permission)

    return userPermission == PackageManager.PERMISSION_GRANTED
}

/**
 * Request permissions
 */

fun Fragment.requestPermissionWithRationale(
    context: Context,
    description: String,
    permission: String,
    requestCode: Int,
    noThanks: () -> Unit
) {
    if (shouldShowRequestPermissionRationale(permission)) {
        context.showPermissionRationale(
            description,
            ok = { requestPermissions(arrayOf(permission), requestCode) },
            noThanks
        )
    } else {
        requestPermissions(arrayOf(permission), requestCode)
    }
}

/**
 * Permissions alert dialog
 */

fun Context.showPermissionRationale(
    description: String,
    ok: () -> Unit,
    noThanks: () -> Unit
) {
    val builder = AlertDialog.Builder(this)
    builder.setMessage(description)
        .setPositiveButton(R.string.ok) { _, _ -> ok() }
        .setNegativeButton(R.string.no_thanks) { _, _ -> noThanks() }

    val dialog = builder.create()
    dialog.show()
}

fun Context.showPermissionAlwaysDeniedExplanation(message: String) {
    val builder = AlertDialog.Builder(this)
    builder.setMessage(message)
        .setPositiveButton(R.string.i_understand) { _, _ -> }
    val dialog = builder.create()
    dialog.show()
}


/**
 * Denied permissions
 */

fun Fragment.permissionIsAlwaysDenied(permission: String): Boolean {
    val shouldShowNotice = shouldShowRequestPermissionRationale(permission)
    return !shouldShowNotice
}
