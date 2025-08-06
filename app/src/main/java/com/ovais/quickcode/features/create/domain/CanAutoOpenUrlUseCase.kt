package com.ovais.quickcode.features.create.domain

import com.ovais.quickcode.storage.db.ConfigurationDao
import com.ovais.quickcode.utils.orFalse
import com.ovais.quickcode.utils.usecase.SuspendUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface CanAutoOpenUrlUseCase : SuspendUseCase<Boolean>
class DefaultCanAutoOpenUrlUseCase(
    private val configurationDao: ConfigurationDao,
    private val dispatcherIO: CoroutineDispatcher
) : CanAutoOpenUrlUseCase {
    override suspend fun invoke(): Boolean {
        return withContext(dispatcherIO) {
            configurationDao.canAutoOpenUrl().orFalse
        }
    }
}