package com.ovais.quickcode.features.settings.domain

import com.ovais.quickcode.locale.LocaleProvider
import com.ovais.quickcode.utils.usecase.UseCase

interface GetLocaleUseCase : UseCase<Map<String, String>>

class DefaultGetLocaleUseCase(
    private val localProvider: LocaleProvider
) : GetLocaleUseCase {
    override fun invoke(): Map<String, String> {
        return localProvider.availableLocales
    }
}