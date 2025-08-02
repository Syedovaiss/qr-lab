package com.ovais.quickcode.utils.usecase

import com.ovais.quickcode.storage.QuickCodeConfigurationManager


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