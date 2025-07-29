package com.ovais.quickcode.features.settings.data

data class AppSettings(
    val foregroundColor: String,
    val backgroundColor: String,
    val qrFormat: String,
    val canVibrateOnScan: Boolean,
    val canBeepOnScan: Boolean,
    val canAutoCopyOnScan: Boolean,
    val canAutoOpenURLOnScan: Boolean,
    val locale: String,
    val canSendAnonymousUsageData: String
)
