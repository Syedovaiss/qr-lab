package com.ovais.quickcode.locale

import java.util.Locale

interface LocaleProvider {
    val availableLocales: Map<String, String>
    fun getLocaleCode(language: String): String
}

class DefaultLocaleProvider : LocaleProvider {
    private companion object {
        private const val DEFAULT_LANGUAGE_CODE = "en"
    }

    val localeMap: Map<String, String> = Locale.getAvailableLocales()
        .filter { it.language.isNotBlank() && it.country.isNotBlank() }
        .distinctBy { "${it.language}-${it.country}" }
        .sortedBy { it.getDisplayName(Locale.ENGLISH) }
        .associate { it.getDisplayName(Locale.ENGLISH) to "${it.language}-${it.country}" }

    override val availableLocales: Map<String, String>
        get() = localeMap

    override fun getLocaleCode(language: String): String {
        return (
                localeMap[localeMap.keys.find { it.contains(language) }]
                    ?: DEFAULT_LANGUAGE_CODE
                )
    }

}