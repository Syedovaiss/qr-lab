package com.ovais.quickcode.utils.usecase

import com.ovais.quickcode.storage.QuickCodeConfigurationManager

interface GetTermsAndConditionsUseCase : UseCase<String>

class DefaultGetTermsAndConditionsUseCase(
    private val appConfigurationManager: QuickCodeConfigurationManager
) : GetTermsAndConditionsUseCase {
    private companion object {
        private const val KEY_TERMS_AND_CONDITIONS_URL = "terms_and_conditions_url"
    }

    override fun invoke(): String {
        return appConfigurationManager.getString(KEY_TERMS_AND_CONDITIONS_URL)
    }
}