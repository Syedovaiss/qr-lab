package com.ovais.quickcode.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.provider.Settings
import android.util.Patterns
import androidx.core.net.toUri
import timber.log.Timber


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
        Timber.e(e)
        // Play Store not installed; fallback to browser
        val webUri = "https://play.google.com/store/apps/details?id=$packageName".toUri()
        this.startActivity(Intent(Intent.ACTION_VIEW, webUri))
    }
}

val systemLocale: String
    get() = Resources.getSystem().configuration.locales.get(0).language

fun Context.restartApp() {
    val intent = this.packageManager.getLaunchIntentForPackage(this.packageName)
    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    this.startActivity(intent)
    if (this is Activity) {
        this.finish()
    }
    Runtime.getRuntime().exit(0)
}

fun Context.shareIntent(uri: Uri) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, uri)
        type = "image/png"
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    val chooser = Intent.createChooser(shareIntent, "Share Barcode")
    chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(chooser)
}

val String.isURL: Boolean
    get() = Patterns.WEB_URL.matcher(this).matches()