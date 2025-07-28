package com.ovais.quickcode.features.home.domain

import android.content.Context
import com.ovais.quickcode.storage.QuickCodePreferenceManager
import com.ovais.quickcode.utils.orFalse
import com.ovais.quickcode.utils.usecase.SuspendUseCase

interface CanLoginUseCase : SuspendUseCase<Boolean>

class DefaultCanLoginUseCase(
    private val context: Context,
    private val preferenceManager: QuickCodePreferenceManager
) : CanLoginUseCase {
    private companion object {
        private const val IS_LOGGED_IN = "is_logged_in"
    }

    override suspend fun invoke(): Boolean {
        return preferenceManager.read(context, IS_LOGGED_IN, Boolean::class.java).orFalse.not()
    }
}