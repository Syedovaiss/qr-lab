package com.ovais.quickcode.features.settings.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import com.ovais.quickcode.core.ui.theme.ColorPrimary
import com.ovais.quickcode.core.ui.theme.ColorTertiary
import com.ovais.quickcode.utils.components.AppSwitch
import com.ovais.quickcode.utils.components.BodyText
import com.ovais.quickcode.utils.components.ColorPickerDialog
import com.ovais.quickcode.utils.components.IconCircle
import com.ovais.quickcode.utils.components.RadioSelectionDialog
import com.ovais.quickcode.utils.components.SubtitleText
import com.ovais.quickcode.utils.components.TopBar
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
    val locale by viewModel.locale.collectAsStateWithLifecycle()
    val clearedMessage = stringResource(R.string.cleared_all_history)
    val clearingMessage = stringResource(R.string.clearing_history)

    LaunchedEffect(Unit) {
        viewModel.isHistoryCleared.collectLatest { isCleared ->
            if (isCleared) {
                viewModel.hideClearHistoryDialog()
                snackbarHostState.showSnackbar(clearedMessage)
            } else {
                viewModel.hideClearHistoryDialog()
                snackbarHostState.showSnackbar(clearingMessage)
            }
        }
    }
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
        TopBar(
            R.string.settings,
            onBack
        )
        Spacer(modifier = Modifier.height(4.dp))

        if (uiState.isLoading) {
            SettingLoadingView()
        } else {

            SettingsSection(title = stringResource(R.string.customization)) {
                SettingRowItem(
                    label = stringResource(R.string.content_color),
                    description = stringResource(R.string.content_color_description)
                ) {
                    RoundedColoredCircle(
                        color = uiState.settings.foregroundColor,
                        onClick = { viewModel.showColorPicker(ColorType.FOREGROUND) }
                    )
                }

                SettingRowItem(
                    label = stringResource(R.string.content_background_color),
                    description = stringResource(R.string.content_background_color_description)
                ) {
                    RoundedColoredCircle(
                        color = uiState.settings.backgroundColor,
                        onClick = { viewModel.showColorPicker(ColorType.BACKGROUND) }
                    )
                }

                SettingRowItem(
                    label = stringResource(R.string.code_export_format),
                    description = stringResource(R.string.code_export_format_description)
                ) {
                    val interactionSource = remember { MutableInteractionSource() }
                    BodyText(
                        uiState.settings.qrFormat,
                        color = ColorTertiary,
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
            SettingsSection(title = stringResource(R.string.user_feedback_and_behaviour)) {
                SettingRowItem(
                    label = stringResource(R.string.vibrate_on_scan),
                    description = stringResource(R.string.vibrate_on_scan_description),
                    trailingAttribute = {
                        AppSwitch(
                            checked = uiState.settings.canVibrateOnScan,
                            onCheckedChange = { viewModel.updateVibrationSetting(it) }
                        )
                    }
                )

                SettingRowItem(
                    label = stringResource(R.string.sound_on_scan),
                    description = stringResource(R.string.sound_on_scan_description),
                    trailingAttribute = {
                        AppSwitch(
                            checked = uiState.settings.canBeepOnScan,
                            onCheckedChange = { viewModel.updateBeepSetting(it) }
                        )
                    }
                )

                SettingRowItem(
                    label = stringResource(R.string.clipboard_setting),
                    description = stringResource(R.string.clipboard_setting_description),
                    trailingAttribute = {
                        AppSwitch(
                            checked = uiState.settings.canAutoCopyOnScan,
                            onCheckedChange = { viewModel.updateAutoCopySetting(it) }
                        )
                    }
                )

                SettingRowItem(
                    label = stringResource(R.string.web_url),
                    description = stringResource(R.string.web_url_description),
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
            SettingsSection(title = stringResource(R.string.history_and_storage)) {
                SettingRowItem(
                    label = stringResource(R.string.clear_history),
                    description = stringResource(R.string.clear_history_description)
                ) {
                    IconCircle(
                        iconRes = R.drawable.ic_delete,
                        backgroundColor = ColorPrimary,
                        size = 36.dp,
                        iconSize = 20.dp
                    ) {
                        viewModel.showClearHistoryDialog()
                    }
                }

                SettingRowItem(
                    label = stringResource(R.string.export_history),
                    description = stringResource(R.string.export_history_description)
                ) {
                    IconCircle(
                        iconRes = R.drawable.ic_right,
                        backgroundColor = ColorPrimary,
                        size = 36.dp,
                        iconSize = 20.dp
                    ) {

                        viewModel.showExportHistoryDialog()
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
            SettingsSection(title = stringResource(R.string.language_and_regional)) {
                SettingRowItem(
                    label = stringResource(R.string.app_language),
                    description = stringResource(R.string.app_language_description)
                ) {
                    val interactionSource = remember { MutableInteractionSource() }
                    BodyText(
                        uiState.settings.locale,
                        color = ColorTertiary,
                        modifier = Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { viewModel.showLanguageDialog() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- Privacy & Permissions ---
            SettingsSection(title = stringResource(R.string.privacy_and_permissions)) {
                SettingRowItem(
                    label = stringResource(R.string.camera_status),
                    description = stringResource(R.string.camera_status_description)
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
                    label = stringResource(R.string.gallery_status),
                    description = stringResource(R.string.gallery_status_description)
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
                    label = stringResource(R.string.privacy_policy),
                    description = stringResource(R.string.privacy_policy_description)
                ) {
                    IconCircle(
                        iconRes = R.drawable.ic_right,
                        backgroundColor = ColorPrimary,
                        size = 36.dp,
                        iconSize = 20.dp
                    ) {
                        viewModel.openPrivacyPolicy()
                    }
                }

                SettingRowItem(
                    label = stringResource(R.string.label_send_anonymous_data),
                    description = stringResource(R.string.desc_send_anonymous_data),
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
            SettingsSection(title = stringResource(R.string.app_info_and_support)) {
                SettingRowItem(
                    label = stringResource(R.string.label_version),
                    description = stringResource(R.string.desc_version)
                ) {
                    BodyText(
                        stringResource(
                            R.string.app_version,
                            appConfig.appVersion,
                            appConfig.versionCode
                        ),
                        color = ColorTertiary
                    )
                }

                SettingRowItem(
                    label = stringResource(R.string.label_about_app),
                    description = stringResource(R.string.desc_about_app)
                ) {
                    IconCircle(
                        iconRes = R.drawable.ic_right,
                        backgroundColor = ColorPrimary,
                        size = 36.dp,
                        iconSize = 20.dp
                    ) {
                        viewModel.openAboutLink()
                    }
                }

                SettingRowItem(
                    label = stringResource(R.string.label_rate_app),
                    description = stringResource(R.string.desc_rate_app)
                ) {
                    IconCircle(
                        iconRes = R.drawable.ic_right,
                        backgroundColor = ColorPrimary,
                        size = 36.dp,
                        iconSize = 20.dp
                    ) {
                        context.openPlayStore()
                    }
                }

                SettingRowItem(
                    label = stringResource(R.string.label_share_app),
                    description = stringResource(R.string.desc_share_app)
                ) {
                    IconCircle(
                        iconRes = R.drawable.ic_right,
                        backgroundColor = ColorPrimary,
                        size = 36.dp,
                        iconSize = 20.dp
                    ) {
                        context.shareApp(
                            title = R.string.share_title,
                            description = R.string.share_description
                        )
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
            title = stringResource(R.string.dialog_title_select_export_format),
            options = listOf(
                stringResource(R.string.option_png),
                stringResource(R.string.option_jpg)
            ),
            selectedOption = uiState.settings.qrFormat,
            optionLabel = { it },
            onOptionSelected = { viewModel.updateQRFormat(it) },
            onDismissRequest = { viewModel.hideExportFormatDialog() }
        )
    }

    if (uiState.showClearHistoryDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.hideClearHistoryDialog() },
            title = { BodyText(stringResource(R.string.delete_history_dialog_title)) },
            text = { BodyText(stringResource(R.string.delete_history_dialog_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearHistory()
                    }
                ) {
                    BodyText(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.hideClearHistoryDialog() }) {
                    BodyText(stringResource(R.string.cancel))
                }
            }
        )
    }

    if (uiState.showExportHistoryDialog) {
        RadioSelectionDialog(
            title = stringResource(R.string.dialog_title_export_history),
            options = listOf(
                stringResource(R.string.option_csv),
                stringResource(R.string.option_pdf)
            ),
            selectedOption = stringResource(R.string.option_csv),
            optionLabel = { it },
            onOptionSelected = { viewModel.exportHistory(it) },
            onDismissRequest = { viewModel.hideExportHistoryDialog() }
        )
    }

    if (uiState.showLanguageDialog) {
        RadioSelectionDialog(
            title = stringResource(R.string.select_language),
            options = locale,
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