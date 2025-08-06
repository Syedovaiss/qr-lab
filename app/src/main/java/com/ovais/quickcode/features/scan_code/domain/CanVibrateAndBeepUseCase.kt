package com.ovais.quickcode.features.scan_code.domain

import com.ovais.quickcode.storage.db.ConfigurationDao
import com.ovais.quickcode.utils.orFalse
import com.ovais.quickcode.utils.usecase.SuspendUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface CanVibrateAndBeepUseCase : SuspendUseCase<Pair<Boolean, Boolean>>

class DefaultCanVibrateAndBeepUseCase(
    private val configurationDao: ConfigurationDao,
    private val dispatcherIO: CoroutineDispatcher
) : CanVibrateAndBeepUseCase {
    override suspend fun invoke(): Pair<Boolean, Boolean> {
        return withContext(dispatcherIO) {
            val canBeep = configurationDao.canPlayBeepSound().orFalse
            val canVibrate = configurationDao.canVibrateOnScan().orFalse
            Pair(canBeep, canVibrate)
        }
    }
}