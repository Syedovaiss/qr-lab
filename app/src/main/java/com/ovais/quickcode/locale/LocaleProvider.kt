package com.ovais.quickcode.locale

interface LocaleProvider {
    val availableLocales: Map<String, String>
    fun getLocaleCode(language: String): String
}

class DefaultLocaleProvider : LocaleProvider {
    private companion object {
        private const val DEFAULT_LANGUAGE_CODE = "en"
        private const val ENGLISH = "en"
        private const val SPANISH = "es"
        private const val FRENCH = "fr"
        private const val GERMAN = "de"
        private const val PORTUGUESE_BRAZILIAN = "pt-BR"
        private const val RUSSIAN = "ru"
        private const val ARABIC = "ar"
        private const val CHINESE_SIMPLIFIED = "zh-CN"
        private const val CHINESE_TRADITIONAL = "zh-TW"
        private const val JAPANESE = "ja"
        private const val KOREAN = "ko"
        private const val HINDI = "hi"
        private const val INDONESIAN = "id"
        private const val TURKISH = "tr"
        private const val ITALIAN = "it"
    }

    val localeMap: Map<String, String> = hashMapOf(
        "English" to ENGLISH,
        "Spanish" to SPANISH,
        "French" to FRENCH,
        "German" to GERMAN,
        "Portuguese (Brazilian)" to PORTUGUESE_BRAZILIAN,
        "Russian" to RUSSIAN,
        "Arabic" to ARABIC,
        "Chinese (Simplified)" to CHINESE_SIMPLIFIED,
        "Chinese (Traditional)" to CHINESE_TRADITIONAL,
        "Japanese" to JAPANESE,
        "Korean" to KOREAN,
        "Hindi" to HINDI,
        "Indonesian" to INDONESIAN,
        "Turkish" to TURKISH,
        "Italian" to ITALIAN
    )

    override val availableLocales: Map<String, String>
        get() = localeMap

    override fun getLocaleCode(language: String): String {
        return (
                localeMap[localeMap.keys.find { it.contains(language) }]
                    ?: DEFAULT_LANGUAGE_CODE
                )
    }

}