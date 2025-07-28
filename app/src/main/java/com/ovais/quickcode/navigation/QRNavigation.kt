package com.ovais.quickcode.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.ovais.quickcode.features.create.presentation.CreateQRView
import com.ovais.quickcode.features.history.presentation.HistoryScreen
import com.ovais.quickcode.features.home.presentation.HomeAction
import com.ovais.quickcode.features.home.presentation.HomeScreenView
import com.ovais.quickcode.features.scan_qr.presentation.ScanQRView
import com.ovais.quickcode.features.settings.presentation.SettingsView

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
                                is HomeAction.OnCreateCode -> Routes.CreateQR
                                is HomeAction.OnSettingsClicked -> Routes.Settings
                                is HomeAction.OnHistoryClicked -> Routes.History
                                is HomeAction.OnScanCode -> Routes.ScanQR
                            }
                            backStack.add(route)
                        }
                    )
                }

                is Routes.ScanQR -> NavEntry(key) {
                    ScanQRView(
                        scaffoldPadding,
                        snackbarHostState = snackBarHostState
                    )
                }

                is Routes.CreateQR -> NavEntry(key) {
                    CreateQRView(
                        scaffoldPadding,
                        snackbarHostState = snackBarHostState
                    )
                }

                is Routes.Settings -> NavEntry(key) {
                    SettingsView(
                        scaffoldPadding
                    )
                }

                is Routes.History -> NavEntry(key) {
                    HistoryScreen()
                }
            }
        }
    )

}