package com.ovais.quickcode.navigation


sealed interface Routes {
    data object Home : Routes
    data object ScanQR : Routes
    data object CreateQR : Routes
    data object Settings : Routes
    data object History : Routes
}