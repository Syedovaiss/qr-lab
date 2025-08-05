package com.ovais.quickcode.features.on_boarding.data

import androidx.annotation.RawRes
import androidx.annotation.StringRes

data class OnboardingPage(
    @param:StringRes val title: Int,
    @param:StringRes val description: Int,
    @param:RawRes val lottieRes: Int
)
