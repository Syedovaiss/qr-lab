package com.ovais.quickcode.features.settings.domain

import com.ovais.quickcode.storage.QuickCodeConfigurationManager
import com.ovais.quickcode.utils.usecase.UseCase


interface GetPrivacyPolicyUseCase : UseCase<String>

class DefaultGetPrivacyPolicyUseCase(
    private val appConfigurationManager: QuickCodeConfigurationManager
) : GetPrivacyPolicyUseCase {
    private companion object {
        private const val KEY_PRIVACY_POLICY = "privacy_policy_url"
    }

    override fun invoke(): String {
        return appConfigurationManager.getString(KEY_PRIVACY_POLICY)
    }
}