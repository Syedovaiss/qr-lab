package com.ovais.quickcode.logger

import com.ovais.quickcode.BuildConfig
import timber.log.Timber

interface AppLogger {
    fun logInfo(vararg message: String)
    fun logException(vararg message: String)
}

class DefaultAppLogger : AppLogger {
    private companion object {
        private const val TAG = "QR-Log:"
    }

    override fun logInfo(vararg message: String) {
        if (BuildConfig.DEBUG) {
            Timber.tag(TAG).i(message.joinToString())
        }
    }

    override fun logException(vararg message: String) {
        if (BuildConfig.DEBUG) {
            Timber.tag(TAG).e(message.joinToString())
        }
    }
}