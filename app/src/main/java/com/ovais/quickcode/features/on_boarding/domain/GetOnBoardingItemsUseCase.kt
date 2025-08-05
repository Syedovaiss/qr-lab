package com.ovais.quickcode.features.on_boarding.domain

import com.ovais.quickcode.R
import com.ovais.quickcode.features.on_boarding.data.OnboardingPage
import com.ovais.quickcode.utils.usecase.UseCase


interface GetOnBoardingItemsUseCase : UseCase<List<OnboardingPage>>

class DefaultGetOnBoardingItemsUseCase : GetOnBoardingItemsUseCase {
    override fun invoke(): List<OnboardingPage> {
        return listOf(
            OnboardingPage(
                title = R.string.on_boarding_title_1,
                description = R.string.on_boarding_description_1,
                lottieRes = R.raw.qr_code
            ),
            OnboardingPage(
                title = R.string.on_boarding_title_2,
                description = R.string.on_boarding_description_2,
                lottieRes = R.raw.scanner
            ),
            OnboardingPage(
                title = R.string.on_boarding_title_3,
                description = R.string.on_boarding_description_3,
                lottieRes = R.raw.no_connection
            )
        )
    }
}