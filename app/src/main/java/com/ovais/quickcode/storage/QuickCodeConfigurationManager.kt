package com.ovais.quickcode.storage

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.ovais.quickcode.BuildConfig
import com.ovais.quickcode.R
import com.ovais.quickcode.logger.AppLogger

interface QuickCodeConfigurationManager {
    suspend fun activate()
    fun getString(key: String): String
    fun getBoolean(key: String): Boolean
    fun getLong(key: String): Long
    fun getDouble(key: String): Double
}

class DefaultQuickCodeConfigurationManager(
    private val logger: AppLogger
) : QuickCodeConfigurationManager {
    val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
    val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = BuildConfig.REMOTE_CONFIG_INTERVAL
    }

    init {
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
    }

    override suspend fun activate() {
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val isUpdated = task.result
                logger.logInfo("Remote Config Enabled:$isUpdated")
            } else {
                logger.logInfo("Remote Config Enabled:false")
            }
        }
    }

    override fun getString(key: String): String {
        return remoteConfig.getString(key)
    }

    override fun getBoolean(key: String): Boolean {
        return remoteConfig.getBoolean(key)
    }

    override fun getLong(key: String): Long {
        return remoteConfig.getLong(key)
    }

    override fun getDouble(key: String): Double {
        return remoteConfig.getDouble(key)
    }
}