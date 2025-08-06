package com.ovais.quickcode.features.on_boarding.domain

import android.content.Context
import com.ovais.quickcode.storage.QuickCodePreferenceManager
import com.ovais.quickcode.utils.usecase.SuspendUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

sealed interface OnBoardingStatus {
    data object Finished : OnBoardingStatus
}

interface UpdateOnBoardingStatusUseCase : SuspendUseCase<OnBoardingStatus>

class DefaultUpdateOnBoardingStatusUseCase(
    private val preferenceManager: QuickCodePreferenceManager,
    private val context: Context,
    private val dispatcherIO: CoroutineDispatcher
) : UpdateOnBoardingStatusUseCase {
    private companion object {
        private const val IS_BOARDING_SEEN = "is_onboarding_seen"
    }

    override suspend fun invoke(): OnBoardingStatus {
        return withContext(dispatcherIO) {
            preferenceManager.save(context, IS_BOARDING_SEEN, true)
            OnBoardingStatus.Finished
        }
    }
}