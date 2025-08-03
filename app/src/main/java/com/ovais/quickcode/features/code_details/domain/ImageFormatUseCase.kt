package com.ovais.quickcode.features.code_details.domain

import com.ovais.quickcode.storage.db.ConfigurationDao
import com.ovais.quickcode.utils.QRFormat
import com.ovais.quickcode.utils.usecase.SuspendUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface ImageFormatUseCase : SuspendUseCase<String>

class DefaultImageFormatUseCase(
    private val configurationDao: ConfigurationDao,
    private val dispatcherIO: CoroutineDispatcher
) : ImageFormatUseCase {
    override suspend fun invoke(): String {
        return withContext(dispatcherIO) {
            configurationDao.getQRFormat() ?: QRFormat.PNG.name
        }
    }
}