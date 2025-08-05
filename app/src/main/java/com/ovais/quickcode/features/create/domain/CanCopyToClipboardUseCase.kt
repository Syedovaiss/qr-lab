package com.ovais.quickcode.features.create.domain

import com.ovais.quickcode.storage.db.ConfigurationDao
import com.ovais.quickcode.utils.orFalse
import com.ovais.quickcode.utils.usecase.SuspendUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface CanCopyToClipboardUseCase : SuspendUseCase<Boolean>
class DefaultCanCopyToClipboardUseCase(
    private val configurationDao: ConfigurationDao,
    private val dispatcherIO: CoroutineDispatcher
) : CanCopyToClipboardUseCase {
    override suspend fun invoke(): Boolean {
        return withContext(dispatcherIO) {
            configurationDao.getConfigurationSync()?.canAutoCopyOnScan.orFalse
        }
    }
}