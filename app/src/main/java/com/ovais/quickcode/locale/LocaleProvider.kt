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
        private const val PORTUGUESE_BRAZILIAN = "pt"
        private const val RUSSIAN = "ru"
        private const val ARABIC = "ar"
        private const val CHINESE_SIMPLIFIED = "zh"
        private const val JAPANESE = "ja"
        private const val KOREAN = "ko"
        private const val HINDI = "hi"
        private const val INDONESIAN = "id"
        private const val TURKISH = "tr"
        private const val ITALIAN = "it"
        private const val URDU = "ur"
        private const val DUTCH = "nl"
        private const val SWEDISH = "sv"
        private const val BENGALI = "bn"
        private const val TELUGU = "te"
        private const val MARATHI = "mr"
        private const val TAMIL = "ta"
        private const val GUJARATI = "gu"
        private const val SINDHI = "sd"
        private const val PASHTO = "ps"
        private const val BALOCHI = "bal"
    }

    val localeMap: Map<String, String> = hashMapOf(
        "Arabic" to ARABIC,
        "Balochi" to BALOCHI,
        "Bengali" to BENGALI,
        "Dutch (Netherlands, Belgium)" to DUTCH,
        "English" to ENGLISH,
        "French" to FRENCH,
        "German" to GERMAN,
        "Gujarati" to GUJARATI,
        "Hindi" to HINDI,
        "Indonesian" to INDONESIAN,
        "Italian" to ITALIAN,
        "Japanese" to JAPANESE,
        "Korean" to KOREAN,
        "Marathi" to MARATHI,
        "Pashto" to PASHTO,
        "Portuguese (Brazilian)" to PORTUGUESE_BRAZILIAN,
        "Sindhi" to SINDHI,
        "Spanish" to SPANISH,
        "Swedish" to SWEDISH,
        "Tamil" to TAMIL,
        "Telugu" to TELUGU,
        "Turkish" to TURKISH,
        "Urdu" to URDU,
        "Russian" to RUSSIAN,
        "Chinese (Simplified)" to CHINESE_SIMPLIFIED
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