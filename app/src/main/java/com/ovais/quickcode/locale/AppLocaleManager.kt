package com.ovais.quickcode.locale

import android.content.Context
import android.content.res.Configuration
import com.ovais.quickcode.utils.systemLocale
import java.util.Locale

fun interface AppLocaleManager {
    fun setLocale(language: String)
}

class DefaultAppLocaleManager(
    private val context: Context,
    private val localeProvider: LocaleProvider
) : AppLocaleManager {
    private companion object {
        private const val SYSTEM = "System"
    }

    override fun setLocale(language: String) {
        val lang = if (language == SYSTEM) {
            systemLocale
        } else localeProvider.getLocaleCode(language)
        val locale = Locale.forLanguageTag(lang)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        context.createConfigurationContext(config)
    }
}