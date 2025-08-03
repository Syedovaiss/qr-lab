package com.ovais.quickcode.features.settings.domain

import androidx.compose.ui.graphics.toArgb
import com.ovais.quickcode.features.settings.data.AppSettings
import com.ovais.quickcode.storage.db.ConfigurationDao
import com.ovais.quickcode.utils.permissions.PermissionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface UpdateSettingUseCase {
    suspend fun updateForegroundColor(color: androidx.compose.ui.graphics.Color)
    suspend fun updateBackgroundColor(color: androidx.compose.ui.graphics.Color)
    suspend fun updateQRFormat(format: String)
    suspend fun updateVibrationSetting(enabled: Boolean)
    suspend fun updateBeepSetting(enabled: Boolean)
    suspend fun updateAutoCopySetting(enabled: Boolean)
    suspend fun updateAutoOpenURLSetting(enabled: Boolean)
    suspend fun updateLocale(locale: String)
    suspend fun updateAnonymousUsageDataSetting(enabled: Boolean)
    fun getSettings(): Flow<AppSettings>
    suspend fun initializeDefaultSettings()
}

class DefaultUpdateSettingUseCase(
    private val configurationDao: ConfigurationDao,
    private val permissionManager: PermissionManager
) : UpdateSettingUseCase {

    override suspend fun updateForegroundColor(color: androidx.compose.ui.graphics.Color) {
        val config = configurationDao.getConfigurationSync()
        config?.let {
            configurationDao.updateForegroundColor(it.id, color.toArgb().toString())
        }
    }

    override suspend fun updateBackgroundColor(color: androidx.compose.ui.graphics.Color) {
        val config = configurationDao.getConfigurationSync()
        config?.let {
            configurationDao.updateBackgroundColor(it.id, color.toArgb().toString())
        }
    }

    override suspend fun updateQRFormat(format: String) {
        val config = configurationDao.getConfigurationSync()
        config?.let {
            configurationDao.updateQRFormat(it.id, format)
        }
    }

    override suspend fun updateVibrationSetting(enabled: Boolean) {
        val config = configurationDao.getConfigurationSync()
        config?.let {
            configurationDao.updateVibrationSetting(it.id, enabled)
        }
    }

    override suspend fun updateBeepSetting(enabled: Boolean) {
        val config = configurationDao.getConfigurationSync()
        config?.let {
            configurationDao.updateBeepSetting(it.id, enabled)
        }
    }

    override suspend fun updateAutoCopySetting(enabled: Boolean) {
        val config = configurationDao.getConfigurationSync()
        config?.let {
            configurationDao.updateAutoCopySetting(it.id, enabled)
        }
    }

    override suspend fun updateAutoOpenURLSetting(enabled: Boolean) {
        val config = configurationDao.getConfigurationSync()
        config?.let {
            configurationDao.updateAutoOpenURLSetting(it.id, enabled)
        }
    }

    override suspend fun updateLocale(locale: String) {
        val config = configurationDao.getConfigurationSync()
        config?.let {
            configurationDao.updateLocale(it.id, locale)
        }
    }

    override suspend fun updateAnonymousUsageDataSetting(enabled: Boolean) {
        val config = configurationDao.getConfigurationSync()
        config?.let {
            configurationDao.updateAnonymousUsageDataSetting(it.id, enabled.toString())
        }
    }

    override fun getSettings(): Flow<AppSettings> {
        return configurationDao.getConfiguration().map { config ->
            config?.let {
                AppSettings.fromLocalConfiguration(it).copy(
                    cameraPermissionGranted = permissionManager.hasCameraPermission,
                    galleryPermissionGranted = permissionManager.hasStoragePermission
                )
            } ?: AppSettings()
        }
    }

    override suspend fun initializeDefaultSettings() {
        val existingConfig = configurationDao.getConfigurationSync()
        if (existingConfig == null) {
            val defaultSettings = AppSettings()
            configurationDao.insertConfiguration(defaultSettings.toLocalConfiguration())
        }
    }
}