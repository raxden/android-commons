package com.raxdenstudios.commons.android.ext

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageInfo
import android.net.ConnectivityManager
import android.os.Environment
import com.raxdenstudios.commons.android.util.Network
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Context.getPackageInfo(): PackageInfo =
    packageManager.getPackageInfo(packageName, 0)

fun Context.createTemporalImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        storageDir      /* directory */
    )
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun Context.getConnectivityManager(): ConnectivityManager =
    this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

fun Context.isNetworkConnected(): Boolean = Network.isNetworkConnected(this)
