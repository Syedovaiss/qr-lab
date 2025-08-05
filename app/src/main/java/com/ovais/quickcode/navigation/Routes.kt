package com.ovais.quickcode.navigation

import android.graphics.Bitmap


sealed interface Routes {
    data object Splash: Routes
    data object OnBoarding : Routes
    data object Home : Routes
    data object ScanQR : Routes
    data object CreateQR : Routes
    data object Settings : Routes
    data object History : Routes
    data class BarcodeDetails(
        val args: Pair<Bitmap?, MutableMap<String, String>>
    ) : Routes
}