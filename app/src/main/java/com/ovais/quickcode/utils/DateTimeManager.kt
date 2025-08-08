package com.ovais.quickcode.utils

import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface DateTimeManager {
    val now: String
    fun parse(dateString: String?): Date?
}

class DefaultDateTimeManager : DateTimeManager {
    private companion object {
        private const val PATTERN = "dd MMM, yyyy hh:mm a"
    }

    private val formatter by lazy {
        SimpleDateFormat(PATTERN, Locale.getDefault())
    }

    override val now: String
        get() = formatter.format(Date())

    override fun parse(dateString: String?): Date? {
        return dateString?.let {
            try {
                formatter.parse(it)
            } catch (e: Exception) {
                Timber.e(e)
                null
            }
        }
    }
}