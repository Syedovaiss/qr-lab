package com.ovais.quickcode.utils.usecase

import com.ovais.quickcode.utils.LocalConfiguration
import com.ovais.quickcode.utils.LocalConfigurationManager

interface LocalConfigurationUseCase : UseCase<LocalConfiguration>

class DefaultLocalConfigurationUseCase(
    private val localConfigurationManager: LocalConfigurationManager
) : LocalConfigurationUseCase {
    override fun invoke(): LocalConfiguration {
        return LocalConfiguration(
            appVersion = localConfigurationManager.appVersion,
            versionCode = localConfigurationManager.versionCode
        )
    }
}