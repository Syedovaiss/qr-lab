package com.ovais.qrlab.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.ovais.qrlab.features.create.presentation.CreateQRView
import com.ovais.qrlab.features.history.presentation.HistoryScreen
import com.ovais.qrlab.features.home.presentation.HomeIntent
import com.ovais.qrlab.features.home.presentation.HomeScreenView
import com.ovais.qrlab.features.scan_qr.presentation.ScanQRView
import com.ovais.qrlab.features.settings.presentation.SettingsView

@Composable
fun QRNavigation(
    scaffoldPadding: PaddingValues = PaddingValues(),
    snackBarHostState: SnackbarHostState
) {
    val backStack = remember { mutableStateListOf<Routes>(Routes.Home) }
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is Routes.Home -> NavEntry(key) {
                    HomeScreenView(
                        scaffoldPadding = scaffoldPadding,
                        onClick = { intent ->
                            val route = when (intent) {
                                is HomeIntent.OnCreateCode -> Routes.CreateQR
                                is HomeIntent.OnSettingsClicked -> Routes.Settings
                                is HomeIntent.OnHistoryClicked -> Routes.History
                                is HomeIntent.OnScanCode -> Routes.ScanQR
                            }
                            backStack.add(route)
                        }
                    )
                }

                is Routes.ScanQR -> NavEntry(key) {
                    ScanQRView(
                        scaffoldPadding
                    )
                }

                is Routes.CreateQR -> NavEntry(key) {
                    CreateQRView(
                        scaffoldPadding,
                        snackbarHostState = snackBarHostState
                    )
                }

                is Routes.Settings -> NavEntry(key) {
                    SettingsView()
                }

                is Routes.History -> NavEntry(key) {
                    HistoryScreen()
                }
            }
        }
    )

}