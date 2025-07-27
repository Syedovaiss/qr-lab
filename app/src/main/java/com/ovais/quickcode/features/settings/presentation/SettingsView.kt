package com.ovais.quickcode.features.settings.presentation

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovais.quickcode.R
import com.ovais.quickcode.features.info.presentation.StaticInfoFullScreenDialog
import com.ovais.quickcode.utils.components.ColorPickerDialog
import com.ovais.quickcode.utils.components.HeadingText
import com.ovais.quickcode.utils.components.RadioSelectionDialog
import com.ovais.quickcode.utils.components.SubtitleText

@Composable
fun SettingsView(scaffoldPadding: PaddingValues) {
    val scrollState = rememberScrollState()
    var canShowAppThemeSelection by remember { mutableStateOf(false) }
    var selectedAppTheme by remember { mutableStateOf("System") }

    var canShowQRCodeColorPickerDialog by remember { mutableStateOf(false) }
    var selectedForegroundColor by remember { mutableStateOf(Color.Black) }

    var canShowQRCodeBackgroundColorPickerDialog by remember { mutableStateOf(false) }
    var selectedBackgroundColor by remember { mutableStateOf(Color.Black) }

    var canShowExportFormatDialog by remember { mutableStateOf(false) }
    var selectedExportFormat by remember { mutableStateOf("PNG") }
    var vibrationCheckedState by remember { mutableStateOf(false) }
    var soundCheckedState by remember { mutableStateOf(false) }
    var autoCopyCheckedState by remember { mutableStateOf(false) }
    var autoOpenURLState by remember { mutableStateOf(false) }
    var canClearHistory by remember { mutableStateOf(false) }
    var canExportHistory by remember { mutableStateOf(false) }
    var selectedHistoryExportFormat by remember { mutableStateOf("CSV") }
    var appLanguage by remember { mutableStateOf("System") }
    var canShowAppLanguage by remember { mutableStateOf(false) }
    var cameraPermissionState by remember { mutableStateOf(true) }
    var galleryPermissionState by remember { mutableStateOf(true) }
    var canShowPrivacyPolicy by remember { mutableStateOf(false) }
    var canSendAnonymouseUsageData by remember { mutableStateOf(false) }
    var canShowAbout by remember { mutableStateOf(false) }
    var canRateApp by remember { mutableStateOf(false) }
    var canShareApp by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(scaffoldPadding)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .verticalScroll(scrollState)
    ) {
        HeadingText(
            stringResource(R.string.settings),
            paddingValues = PaddingValues(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        // --- Customization Section ---
        SettingsSection(title = stringResource(R.string.customization)) {
            val appThemeInteractionSource = remember { MutableInteractionSource() }
            val foregroundColorInteractionSource = remember { MutableInteractionSource() }
            val backgroundColorInteractionSource = remember { MutableInteractionSource() }
            val exportFormatInteractionSource = remember { MutableInteractionSource() }
            SettingRowItem(icon = R.drawable.ic_palette, label = "App Theme") {
                Text(
                    selectedAppTheme,
                    modifier = Modifier.clickable(
                        appThemeInteractionSource,
                        LocalIndication.current
                    ) {
                        canShowAppThemeSelection = true
                    }
                )
            }

            SettingRowItem(icon = R.drawable.ic_color, label = "Default QR Code Color") {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(selectedForegroundColor)
                        .clickable(
                            foregroundColorInteractionSource,
                            LocalIndication.current
                        ) {
                            canShowQRCodeColorPickerDialog = true
                        }
                )
            }

            SettingRowItem(icon = R.drawable.ic_color_fill, label = "Default QR Code Background") {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(selectedBackgroundColor)
                        .clickable(
                            backgroundColorInteractionSource,
                            LocalIndication.current
                        ) {
                            canShowQRCodeBackgroundColorPickerDialog = true
                        }
                )
            }

            SettingRowItem(icon = R.drawable.ic_save, label = "Default QR Export Format") {
                Text(
                    selectedExportFormat,
                    modifier = Modifier.clickable(
                        exportFormatInteractionSource,
                        LocalIndication.current
                    ) {
                        canShowExportFormatDialog = true
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp)) // slightly reduced
        // --- User Feedback & Behavior ---
        SettingsSection(title = "User Feedback & Behavior") {
            SettingRowItem(
                icon = R.drawable.ic_vibration,
                label = "Vibration on Scan",
                trailingAttribute = {
                    Switch(checked = vibrationCheckedState, onCheckedChange = {
                        vibrationCheckedState = it
                    })
                }
            )

            SettingRowItem(
                icon = R.drawable.ic_volume,
                label = "Beep Sound on Scan",
                trailingAttribute = {
                    Switch(
                        checked = soundCheckedState,
                        onCheckedChange = { soundCheckedState = it })
                }
            )

            SettingRowItem(
                icon = R.drawable.ic_copy,
                label = "Auto Copy Scan Result to Clipboard",
                trailingAttribute = {
                    Switch(checked = autoCopyCheckedState, onCheckedChange = {
                        autoCopyCheckedState = it
                    })
                }
            )

            SettingRowItem(
                icon = R.drawable.ic_browser,
                label = "Auto Open URLs After Scan",
                trailingAttribute = {
                    Switch(checked = autoOpenURLState, onCheckedChange = {
                        autoOpenURLState = it
                    })
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        // --- History & Storage ---
        SettingsSection(title = "History & Storage") {
            SettingRowItem(icon = R.drawable.ic_delete, label = "Clear All Scan History") {
                Text("Delete")
            }

            SettingRowItem(icon = R.drawable.ic_download, label = "Export History") {
                Text("CSV")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        // --- Language & Regional ---
        SettingsSection(title = "Language & Regional") {
            SettingRowItem(icon = R.drawable.ic_lanugage, label = "App Language") {
                Text("System")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        // --- Privacy & Permissions ---
        SettingsSection(title = "Privacy & Permissions") {
            SettingRowItem(icon = R.drawable.ic_camera, label = "Camera Permission Status") {
                Text("Granted")
            }

            SettingRowItem(icon = R.drawable.ic_image, label = "Gallery Permission Status") {
                Text("Granted")
            }

            SettingRowItem(icon = R.drawable.ic_privacy, label = "Privacy Policy") {
                Text("Granted")
            }

            SettingRowItem(icon = R.drawable.ic_bar_chart, label = "Send Anonymous Usage Data") {
                Switch(checked = canSendAnonymouseUsageData, onCheckedChange = {
                    canSendAnonymouseUsageData = it
                })
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        // --- App Info & Support ---
        SettingsSection(title = "App Info & Support") {
            SettingRowItem(icon = R.drawable.ic_info, label = "App Version and Build Info") {
                Text("v1.0.0(100)")
            }

            SettingRowItem(icon = R.drawable.ic_help, label = "About the App") {}

            SettingRowItem(icon = R.drawable.ic_rate, label = "Rate this App") {}

            SettingRowItem(icon = R.drawable.ic_share, label = "Share This App") {}
        }

        Spacer(modifier = Modifier.height(24.dp))
        if (canShowAppThemeSelection) {
            RadioSelectionDialog(
                title = "Select App Theme",
                options = listOf("System", "Dark", "Light"),
                selectedOption = selectedAppTheme,
                optionLabel = { it },
                onOptionSelected = { selectedAppTheme = it },
                onDismissRequest = { canShowAppThemeSelection = false }
            )
        }
        if (canShowQRCodeColorPickerDialog) {
            ColorPickerDialog(
                onColorSelected = {
                    selectedForegroundColor = it
                    canShowQRCodeColorPickerDialog = false
                },
                onDismiss = {
                    canShowQRCodeColorPickerDialog = false
                }
            )
        }
        if (canShowQRCodeBackgroundColorPickerDialog) {
            ColorPickerDialog(
                onColorSelected = {
                    selectedBackgroundColor = it
                    canShowQRCodeBackgroundColorPickerDialog = false
                },
                onDismiss = {
                    canShowQRCodeBackgroundColorPickerDialog = false
                }
            )
        }
        if (canShowExportFormatDialog) {
            RadioSelectionDialog(
                title = "Select Export Format",
                options = listOf("PNG", "JPG/JPEG"),
                selectedOption = selectedExportFormat,
                optionLabel = { it },
                onOptionSelected = { selectedExportFormat = it },
                onDismissRequest = { canShowExportFormatDialog = false }
            )
        }
        if (canClearHistory) {
            AlertDialog(
                onDismissRequest = {

                },
                title = { Text(stringResource(R.string.delete_history_dialog_title)) },
                text = { Text(stringResource(R.string.delete_history_dialog_message)) },
                confirmButton = {
                    TextButton(onClick = {
                        // delete history
                        canClearHistory = false
                    }) {
                        Text(stringResource(R.string.delete))
                    }
                }
            )
        }
        if (canExportHistory) {
            RadioSelectionDialog(
                title = "Export History",
                options = listOf("CSV", "PDF"),
                selectedOption = "CSV",
                optionLabel = { it },
                onOptionSelected = { selectedHistoryExportFormat = it },
                onDismissRequest = { canExportHistory = false }
            )
        }
        if (canShowAppLanguage) {
            RadioSelectionDialog(
                title = "Select Language",
                options = listOf("System", "English", "Urdu"),
                selectedOption = "System",
                optionLabel = { it },
                onOptionSelected = { appLanguage = it },
                onDismissRequest = { canShowAppLanguage = false }
            )
        }
        StaticInfoFullScreenDialog(
            canShowPrivacyPolicy,
            ""
        ) {
            canShowPrivacyPolicy = false
        }
        StaticInfoFullScreenDialog(
            canShowAbout,
            ""
        ) {
            canShowAbout = false
        }

    }
}

@Composable
private fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp), // tighter
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
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
    SettingsView(PaddingValues(16.dp))
}