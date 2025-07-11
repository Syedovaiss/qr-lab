package com.ovais.qrlab.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.ovais.qrlab.features.create_qr.presentation.CreateQRView
import com.ovais.qrlab.features.home.presentation.HomeScreenView
import com.ovais.qrlab.features.scan_qr.presentation.ScanQRView
import com.ovais.qrlab.features.settings.presentation.SettingsView

@Composable
fun QRNavigation(
    scaffoldPadding: PaddingValues = PaddingValues()
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
                        onCreateQR = {
                            backStack.add(Routes.CreateQR)
                        },
                        onScanQR = {
                            backStack.add(Routes.ScanQR)
                        },
                        onSettingsClicked = {
                            backStack.add(Routes.Settings)
                        }
                    )
                }

                is Routes.ScanQR -> NavEntry(key) {
                    ScanQRView()
                }

                is Routes.CreateQR -> NavEntry(key) {
                    CreateQRView()
                }

                is Routes.Settings -> NavEntry(key) {
                    SettingsView()
                }
            }
        }
    )

}