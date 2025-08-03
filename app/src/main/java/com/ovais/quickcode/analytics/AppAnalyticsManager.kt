package com.ovais.quickcode.analytics

import android.os.Bundle
import androidx.core.os.bundleOf
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics

interface AppAnalyticsManager {
    fun logEvent(eventType: String, message: String)
    fun logEvent(eventType: String, message: String, params: HashMap<String, Any>)
}

class DefaultAppAnalyticsManager : AppAnalyticsManager {

    private companion object {
        private const val KEY_MESSAGE = "message"
    }

    private val analytics by lazy {
        Firebase.analytics
    }

    override fun logEvent(eventType: String, message: String) {
        analytics.logEvent(eventType, bundleOf(KEY_MESSAGE to message))
    }

    override fun logEvent(
        eventType: String,
        message: String,
        params: HashMap<String, Any>
    ) {
        val bundleMap = Bundle().apply {
            putString(KEY_MESSAGE, message)
            params.forEach { data ->
                putString(data.key, data.value.toString())
            }
        }
        analytics.logEvent(eventType, bundleMap)
    }
}