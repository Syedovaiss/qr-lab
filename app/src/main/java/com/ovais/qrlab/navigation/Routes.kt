package com.ovais.qrlab.navigation


sealed interface Routes {
    data object Home : Routes
    data object ScanQR : Routes
    data object CreateQR : Routes
    data object Settings : Routes
}