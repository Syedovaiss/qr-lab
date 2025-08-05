package com.ovais.quickcode.core.app.domain

import android.content.Context
import com.ovais.quickcode.navigation.Routes
import com.ovais.quickcode.storage.QuickCodePreferenceManager
import com.ovais.quickcode.utils.orFalse
import com.ovais.quickcode.utils.usecase.SuspendUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface StartDestinationUseCase : SuspendUseCase<Routes>


class DefaultStartDestinationUseCase(
    private val preferenceManager: QuickCodePreferenceManager,
    private val context: Context,
    private val dispatcherIO: CoroutineDispatcher
) : StartDestinationUseCase {
    private companion object {
        private const val IS_BOARDING_SEEN = "is_onboarding_seen"
    }

    override suspend fun invoke(): Routes {
        return withContext(dispatcherIO) {
            val isOnBoardingSeen =
                preferenceManager.read(context, IS_BOARDING_SEEN, Boolean::class.java).orFalse
            if (isOnBoardingSeen) Routes.Home else Routes.OnBoarding
        }

    }
}