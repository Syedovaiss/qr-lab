package com.ovais.quickcode.features.settings.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ovais.quickcode.R
import com.ovais.quickcode.utils.components.AppSwitch
import com.ovais.quickcode.utils.components.BackIcon
import com.ovais.quickcode.utils.components.ColorPickerDialog
import com.ovais.quickcode.utils.components.HeadingText
import com.ovais.quickcode.utils.components.RadioSelectionDialog
import com.ovais.quickcode.utils.components.SubtitleText
import com.ovais.quickcode.utils.openPlayStore
import com.ovais.quickcode.utils.openURL
import com.ovais.quickcode.utils.restartApp
import com.ovais.quickcode.utils.shareApp
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingScreen(
    scaffoldPadding: PaddingValues,
    onBack: () -> Unit,
    viewModel: SettingViewModel = koinViewModel()
) {
    SettingsView(scaffoldPadding, onBack, viewModel)
}

@Composable
fun SettingsView(
    scaffoldPadding: PaddingValues,
    onBack: () -> Unit,
    viewModel: SettingViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = SnackbarHostState()
    val context = LocalContext.current
    val appConfig by viewModel.appConfig.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }
    LaunchedEffect(Unit) {
        viewModel.initSettings()
    }
    LaunchedEffect(Unit) {
        viewModel.privacyPolicyUrl.collectLatest {
            context.openURL(it)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.aboutUsUrl.collectLatest {
            context.openURL(it)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.canRestartApp.collectLatest {
            if (it) {
                context.restartApp()
            }
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(scaffoldPadding)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .verticalScroll(scrollState)
    ) {
        Row {
            BackIcon(onBack)
            HeadingText(
                stringResource(R.string.settings),
                paddingValues = PaddingValues(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        if (uiState.isLoading) {
            SettingLoadingView()
        } else {

            SettingsSection(title = stringResource(R.string.customization)) {
                SettingRowItem(
                    label = "QR Color",
                    description = "Default QR Code Color"
                ) {
                    RoundedColoredCircle(
                        color = uiState.settings.foregroundColor,
                        onClick = { viewModel.showColorPicker(ColorType.FOREGROUND) }
                    )
                }

                SettingRowItem(
                    label = "QR Background",
                    description = "Default QR Code Background"
                ) {
                    RoundedColoredCircle(
                        color = uiState.settings.backgroundColor,
                        onClick = { viewModel.showColorPicker(ColorType.BACKGROUND) }
                    )
                }

                SettingRowItem(
                    label = "Export Format",
                    description = "QR Export Format"
                ) {
                    val interactionSource = remember { MutableInteractionSource() }
                    Text(
                        uiState.settings.qrFormat,
                        modifier = Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { viewModel.showExportFormatDialog() }
                    )
                }
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- User Feedback & Behavior ---
            SettingsSection(title = "User Feedback & Behavior") {
                SettingRowItem(
                    label = "Vibration on Scan",
                    description = "Vibrate when scanned code",
                    trailingAttribute = {
                        AppSwitch(
                            checked = uiState.settings.canVibrateOnScan,
                            onCheckedChange = { viewModel.updateVibrationSetting(it) }
                        )
                    }
                )

                SettingRowItem(
                    label = "Sound on Scan",
                    description = "Play beep sound when scanned",
                    trailingAttribute = {
                        AppSwitch(
                            checked = uiState.settings.canBeepOnScan,
                            onCheckedChange = { viewModel.updateBeepSetting(it) }
                        )
                    }
                )

                SettingRowItem(
                    label = "Copy to clipboard",
                    description = "Auto Copy Scan Result to Clipboard",
                    trailingAttribute = {
                        AppSwitch(
                            checked = uiState.settings.canAutoCopyOnScan,
                            onCheckedChange = { viewModel.updateAutoCopySetting(it) }
                        )
                    }
                )

                SettingRowItem(
                    label = "Open URL",
                    description = "Auto Open URLs After Scan",
                    trailingAttribute = {
                        AppSwitch(
                            checked = uiState.settings.canAutoOpenURLOnScan,
                            onCheckedChange = { viewModel.updateAutoOpenURLSetting(it) }
                        )
                    }
                )
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- History & Storage ---
            SettingsSection(title = "History & Storage") {
                SettingRowItem(
                    label = "Clear History",
                    description = "Clear All Scan History"
                ) {
                    Button(
                        onClick = {
                            viewModel.showClearHistoryDialog()
                        }
                    ) {
                        Text("Clear")
                    }
                }

                SettingRowItem(
                    label = "Export History",
                    description = "Export history to phone"
                ) {
                    Button(
                        onClick = {
                            viewModel.showExportHistoryDialog()
                        }
                    ) {
                        Text("Export")
                    }
                }
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- Language & Regional ---
            SettingsSection(title = "Language & Regional") {
                SettingRowItem(
                    label = "App Language",
                    description = "App locale"
                ) {
                    val interactionSource = remember { MutableInteractionSource() }
                    Text(
                        uiState.settings.locale,
                        modifier = Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { viewModel.showLanguageDialog() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- Privacy & Permissions ---
            SettingsSection(title = "Privacy & Permissions") {
                SettingRowItem(
                    label = "Camera Status",
                    description = "Camera Permission Status"
                ) {
                    val icon =
                        if (uiState.settings.cameraPermissionGranted) R.drawable.ic_tick_green else R.drawable.ic_cross_red
                    Image(
                        painter = painterResource(icon),
                        null,
                        modifier = Modifier.size(32.dp)
                    )
                }

                SettingRowItem(
                    label = "Gallery Status",
                    description = "Gallery Permission Status"
                ) {
                    val icon =
                        if (uiState.settings.galleryPermissionGranted) R.drawable.ic_tick_green else R.drawable.ic_cross_red
                    Image(
                        painter = painterResource(icon),
                        null,
                        modifier = Modifier.size(32.dp)
                    )
                }

                SettingRowItem(
                    label = "Privacy Policy",
                    description = "View privacy policy"
                ) {

                    Button(
                        onClick = {
                            viewModel.openPrivacyPolicy()
                        }
                    ) {
                        Text("Rate")
                    }
                }

                SettingRowItem(
                    label = "Send Anonymous Usage Data",
                    description = "Send data for better stats",
                    trailingAttribute = {
                        AppSwitch(
                            checked = uiState.settings.canSendAnonymousUsageData,
                            onCheckedChange = { viewModel.updateAnonymousUsageDataSetting(it) }
                        )
                    }
                )
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- App Info & Support ---
            SettingsSection(title = "App Info & Support") {
                SettingRowItem(
                    label = "Version",
                    description = "App Version and Build Info"
                ) {
                    Text(
                        stringResource(
                            R.string.app_version,
                            appConfig.appVersion,
                            appConfig.versionCode
                        )
                    )
                }

                SettingRowItem(
                    label = "About the App",
                    description = "Details about the app"
                ) {
                    Button(
                        onClick = {
                            viewModel.openAboutLink()
                        }
                    ) {
                        Text("View")
                    }
                }

                SettingRowItem(
                    label = "Rate this App",
                    description = "Rate app on Play Store"
                ) {
                    Button(
                        onClick = {
                            context.openPlayStore()
                        }
                    ) {
                        Text("Rate")
                    }
                }

                SettingRowItem(
                    label = "Share This App",
                    description = "Share app to friends and family"
                ) {
                    Button(
                        onClick = {
                            context.shareApp(
                                title = R.string.share_title,
                                description = R.string.share_description
                            )
                        }
                    ) {
                        Text("Rate")
                    }
                }
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Snackbar for error messages
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier.padding(16.dp)
    )

    // Dialogs
    if (uiState.showColorPicker) {
        ColorPickerDialog(
            onColorSelected = { color ->
                when (uiState.selectedColorType) {
                    ColorType.FOREGROUND -> viewModel.updateForegroundColor(color)
                    ColorType.BACKGROUND -> viewModel.updateBackgroundColor(color)
                }
                viewModel.hideColorPicker()
            },
            onDismiss = { viewModel.hideColorPicker() }
        )
    }

    if (uiState.showExportFormatDialog) {
        RadioSelectionDialog(
            title = "Select Export Format",
            options = listOf("PNG", "JPG/JPEG"),
            selectedOption = uiState.settings.qrFormat,
            optionLabel = { it },
            onOptionSelected = { viewModel.updateQRFormat(it) },
            onDismissRequest = { viewModel.hideExportFormatDialog() }
        )
    }

    if (uiState.showClearHistoryDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.hideClearHistoryDialog() },
            title = { Text(stringResource(R.string.delete_history_dialog_title)) },
            text = { Text(stringResource(R.string.delete_history_dialog_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearHistory()
                    }
                ) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.hideClearHistoryDialog() }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (uiState.showExportHistoryDialog) {
        RadioSelectionDialog(
            title = "Export History",
            options = listOf("CSV", "PDF"),
            selectedOption = "CSV",
            optionLabel = { it },
            onOptionSelected = { viewModel.exportHistory(it) },
            onDismissRequest = { viewModel.hideExportHistoryDialog() }
        )
    }

    if (uiState.showLanguageDialog) {
        RadioSelectionDialog(
            title = "Select Language",
            options = listOf("System", "English", "Urdu"),
            selectedOption = uiState.settings.locale,
            optionLabel = { it },
            onOptionSelected = { viewModel.updateLocale(it) },
            onDismissRequest = { viewModel.hideLanguageDialog() }
        )
    }
}

@Composable
private fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Column {
        SubtitleText(
            title,
            paddingValues = PaddingValues(top = 12.dp, start = 12.dp, end = 12.dp, bottom = 4.dp)
        )
        content()
    }
}


@Preview(showBackground = true)
@Composable
private fun SettingsPreview() {
    SettingsView(PaddingValues(16.dp), onBack = {})
}