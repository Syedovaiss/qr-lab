package com.ovais.quickcode.logger

import com.ovais.quickcode.BuildConfig
import timber.log.Timber

interface QRLogger {
    fun logInfo(vararg message: String)
    fun logException(vararg message: String)
}

class DefaultQRLogger : QRLogger {
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