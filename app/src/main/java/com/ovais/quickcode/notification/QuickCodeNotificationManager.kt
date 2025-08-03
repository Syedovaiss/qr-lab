package com.ovais.quickcode.notification

import android.content.Context
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel
import com.ovais.quickcode.BuildConfig

fun interface QuickCodeNotificationManager {
    fun init()
}

class DefaultQuickCodeNotificationManager(
    private val context: Context
) : QuickCodeNotificationManager {
    override fun init() {
        if (BuildConfig.DEBUG) {
            OneSignal.Debug.logLevel = LogLevel.VERBOSE
        }
        OneSignal.initWithContext(context.applicationContext, context.packageName)
    }
}