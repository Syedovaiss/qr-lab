package com.ovais.quickcode.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.ovais.quickcode.features.code_details.presentation.BarcodeDetailsScreen
import com.ovais.quickcode.features.create.presentation.CreateQRView
import com.ovais.quickcode.features.history.presentation.HistoryScreen
import com.ovais.quickcode.features.home.presentation.HomeAction
import com.ovais.quickcode.features.home.presentation.HomeScreenView
import com.ovais.quickcode.features.on_boarding.presentation.OnboardingScreen
import com.ovais.quickcode.features.scan_code.presentation.ScanQRView
import com.ovais.quickcode.features.settings.presentation.SettingScreen
import com.ovais.quickcode.features.splash.presentation.SplashView

@Composable
fun QuickCodeNavigation(
    scaffoldPadding: PaddingValues = PaddingValues(),
    snackBarHostState: SnackbarHostState
) {
    val backStack = remember { mutableStateListOf<Routes>(Routes.Splash) }
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is Routes.Splash -> NavEntry(key) {
                    SplashView(
                        onNavigate = { route ->
                            backStack.add(route)
                        }
                    )
                }

                is Routes.OnBoarding -> NavEntry(key) {
                    OnboardingScreen {
                        backStack.add(Routes.Home)
                    }
                }

                is Routes.Home -> NavEntry(key) {
                    HomeScreenView(
                        scaffoldPadding = scaffoldPadding,
                        snackBarHostState = snackBarHostState,
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
                        snackbarHostState = snackBarHostState,
                        onBack = { backStack.removeLastOrNull() }
                    )
                }

                is Routes.CreateQR -> NavEntry(key) {
                    CreateQRView(
                        scaffoldPadding,
                        snackbarHostState = snackBarHostState,
                        onCodeScanned = {
                            backStack.add(Routes.BarcodeDetails(it))
                        },
                        onBack = { backStack.removeLastOrNull() }
                    )
                }

                is Routes.Settings -> NavEntry(key) {
                    SettingScreen(
                        scaffoldPadding,
                        onBack = { backStack.removeLastOrNull() }
                    )
                }

                is Routes.History -> NavEntry(key) {
                    HistoryScreen(
                        onBack = { backStack.removeLastOrNull() }
                    )
                }

                is Routes.BarcodeDetails -> NavEntry(key) {
                    BarcodeDetailsScreen(
                        data = key.args,
                        onBack = { backStack.removeLastOrNull() }
                    )
                }
            }
        }
    )
}