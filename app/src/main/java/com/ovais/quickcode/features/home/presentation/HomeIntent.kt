package com.ovais.quickcode.features.home.presentation

sealed interface HomeIntent {
    data object OnCreateCode : HomeIntent
    data object OnScanCode : HomeIntent
    data object OnHistoryClicked : HomeIntent
    data object OnSettingsClicked : HomeIntent
}