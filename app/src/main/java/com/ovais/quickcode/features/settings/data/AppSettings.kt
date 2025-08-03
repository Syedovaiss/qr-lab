package com.ovais.quickcode.features.settings.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.Color as ComposeColor

data class AppSettings(
    val foregroundColor: Color = Color.Black,
    val backgroundColor: Color = Color.White,
    val qrFormat: String = "PNG",
    val canVibrateOnScan: Boolean = true,
    val canBeepOnScan: Boolean = true,
    val canAutoCopyOnScan: Boolean = false,
    val canAutoOpenURLOnScan: Boolean = false,
    val locale: String = "System",
    val canSendAnonymousUsageData: Boolean = false,
    val cameraPermissionGranted: Boolean = false,
    val galleryPermissionGranted: Boolean = false
) {
    fun toLocalConfiguration(): com.ovais.quickcode.storage.data.LocalConfiguration {
        return com.ovais.quickcode.storage.data.LocalConfiguration(
            foregroundColor = foregroundColor.toArgb().toString(),
            backgroundColor = backgroundColor.toArgb().toString(),
            qrFormat = qrFormat,
            canVibrateOnScan = canVibrateOnScan,
            canBeepOnScan = canBeepOnScan,
            canAutoCopyOnScan = canAutoCopyOnScan,
            canAutoOpenURLOnScan = canAutoOpenURLOnScan,
            appLanguage = locale,
            canSendAnonymousUsageData = canSendAnonymousUsageData.toString()
        )
    }
    
    companion object {
        fun fromLocalConfiguration(config: com.ovais.quickcode.storage.data.LocalConfiguration): AppSettings {
            return AppSettings(
                foregroundColor = ComposeColor(config.foregroundColor.toInt()),
                backgroundColor = ComposeColor(config.backgroundColor.toInt()),
                qrFormat = config.qrFormat,
                canVibrateOnScan = config.canVibrateOnScan,
                canBeepOnScan = config.canBeepOnScan,
                canAutoCopyOnScan = config.canAutoCopyOnScan,
                canAutoOpenURLOnScan = config.canAutoOpenURLOnScan,
                locale = config.appLanguage,
                canSendAnonymousUsageData = config.canSendAnonymousUsageData.toBoolean()
            )
        }
    }
}
