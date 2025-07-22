package com.ovais.qrlab.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings


fun Context.openAppSettings() {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", this.packageName, null)
    )
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    this.startActivity(intent)
}

val Int?.default: Int
    get() = this ?: 0