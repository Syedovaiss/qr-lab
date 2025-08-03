package com.ovais.quickcode.utils.local_config

import com.ovais.quickcode.BuildConfig

interface LocalConfigurationManager {
    val appVersion: String
    val versionCode: String
}

class DefaultLocalConfigurationManager : LocalConfigurationManager {
    override val appVersion: String
        get() = BuildConfig.VERSION_NAME
    override val versionCode: String
        get() = BuildConfig.VERSION_CODE.toString()
}