package com.ovais.quickcode.features.home.presentation

sealed interface HomeAction {
    data object OnCreateCode : HomeAction
    data object OnScanCode : HomeAction
    data object OnHistoryClicked : HomeAction
    data object OnSettingsClicked : HomeAction
}