package com.ovais.quickcode.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.ui.graphics.Color
import androidx.core.net.toUri


fun Context.openAppSettings() {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", this.packageName, null)
    )
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    this.startActivity(intent)
}

val Int?.orZero: Int
    get() = this ?: 0

val Boolean?.orFalse: Boolean
    get() = this ?: false
val String?.orEmpty: String
    get() = this ?: EMPTY_STRING

fun Context.openURL(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    startActivity(intent)
}