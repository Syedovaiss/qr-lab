package com.ovais.quickcode.features.splash.domain

import android.content.Context
import com.ovais.quickcode.locale.AppLocaleManager
import com.ovais.quickcode.storage.db.ConfigurationDao
import com.ovais.quickcode.utils.usecase.SuspendUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface UpdateLocaleUseCase : SuspendUseCase<Context?>

class DefaultUpdateLocaleUseCase(
    private val appLocaleManager: AppLocaleManager,
    private val configurationDao: ConfigurationDao,
    private val dispatcherIO: CoroutineDispatcher
) : UpdateLocaleUseCase {
    override suspend fun invoke(): Context? {
        return withContext(dispatcherIO) {
            configurationDao.getLocale()?.let {
                appLocaleManager.setLocale(it)
            }
        }
    }
}