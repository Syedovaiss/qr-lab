package com.ovais.quickcode.analytics

import android.os.Bundle
import androidx.core.os.bundleOf
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.ovais.quickcode.storage.db.ConfigurationDao
import com.ovais.quickcode.utils.orFalse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface AppAnalyticsManager {
    fun logEvent(eventType: String, message: String)
    fun logEvent(eventType: String, message: String, params: HashMap<String, Any>)
}

class DefaultAppAnalyticsManager(
    private val configurationDao: ConfigurationDao,
    dispatcherIO: CoroutineDispatcher
) : AppAnalyticsManager {

    private var canLogEvent = false
    private val scope = CoroutineScope(dispatcherIO)

    init {
        canLogEvent()
    }

    private companion object {
        private const val KEY_MESSAGE = "message"
    }

    private val analytics by lazy {
        Firebase.analytics
    }

    private fun canLogEvent() {
        scope.launch {
            canLogEvent = configurationDao.canAddAnalytics()?.toBooleanStrictOrNull().orFalse
        }
    }

    override fun logEvent(eventType: String, message: String) {
        if (canLogEvent) {
            analytics.logEvent(eventType, bundleOf(KEY_MESSAGE to message))
        }
    }

    override fun logEvent(
        eventType: String,
        message: String,
        params: HashMap<String, Any>
    ) {
        if (canLogEvent) {
            val bundleMap = Bundle().apply {
                putString(KEY_MESSAGE, message)
                params.forEach { data ->
                    putString(data.key, data.value.toString())
                }
            }
            analytics.logEvent(eventType, bundleMap)
        }
    }
}