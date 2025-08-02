package com.ovais.quickcode.utils.usecase

import com.ovais.quickcode.storage.QuickCodeConfigurationManager

interface GetAboutUsUseCase : UseCase<String>

class DefaultGetAboutUsUseCase(
    private val appConfigurationManager: QuickCodeConfigurationManager
) : GetAboutUsUseCase {
    private companion object {
        private const val KEY_ABOUT_URL = "about_url"
    }

    override fun invoke(): String {
        return appConfigurationManager.getString(KEY_ABOUT_URL).trim()
    }
}