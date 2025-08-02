package com.ovais.quickcode.utils

import android.content.ActivityNotFoundException
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
    if (url.isNotBlank()) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        startActivity(intent)
    }
}

fun Context.shareApp(
    title: Int,
    description: Int
) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, this@shareApp.getString(title))
        putExtra(
            Intent.EXTRA_TEXT,
            "${this@shareApp.getString(description)}\n\n" +
                    "https://play.google.com/store/apps/details?id=${this@shareApp.packageName}"
        )
    }

    this.startActivity(
        Intent.createChooser(shareIntent, "Share App via")
    )
}

fun Context.openPlayStore() {
    val packageName = this.packageName
    val uri = "market://details?id=$packageName".toUri()
    val goToMarketIntent = Intent(Intent.ACTION_VIEW, uri).apply {
        addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
    }

    try {
        this.startActivity(goToMarketIntent)
    } catch (e: ActivityNotFoundException) {
        // Play Store not installed; fallback to browser
        val webUri = "https://play.google.com/store/apps/details?id=$packageName".toUri()
        this.startActivity(Intent(Intent.ACTION_VIEW, webUri))
    }
}