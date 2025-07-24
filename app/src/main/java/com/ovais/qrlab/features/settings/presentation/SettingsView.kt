package com.ovais.qrlab.features.settings.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovais.qrlab.R
import com.ovais.qrlab.utils.components.HeadingText
import com.ovais.qrlab.utils.components.SubtitleText
@Composable
fun SettingsView(scaffoldPadding: PaddingValues) {
    val scrollState = rememberScrollState()
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
        Spacer(modifier = Modifier.height(4.dp)) // reduced
        // --- Customization Section ---
        SettingsSection(title = stringResource(R.string.customization)) {
            SettingRowItem(icon = R.drawable.ic_palette, label = "App Theme") {
                Text("System")
            }
            HorizontalDivider()
            SettingRowItem(icon = R.drawable.ic_color, label = "Default QR Code Color") {
                Text("Black")
            }
            HorizontalDivider()
            SettingRowItem(icon = R.drawable.ic_color_fill, label = "Default QR Code Background") {
                Text("Black")
            }
            HorizontalDivider()
            SettingRowItem(icon = R.drawable.ic_image, label = "Default Logo on QR Code") {
                Image(painterResource(R.drawable.ic_right), null)
            }
            HorizontalDivider()
            SettingRowItem(icon = R.drawable.ic_save, label = "Default QR Export Format") {
                Text("PNG")
            }
        }

        Spacer(modifier = Modifier.height(12.dp)) // slightly reduced
        // --- User Feedback & Behavior ---
        SettingsSection(title = "User Feedback & Behavior") {
            SettingRowItem(
                icon = R.drawable.ic_vibration,
                label = "Vibration on Scan",
                trailingAttribute = {
                    Switch(checked = true, onCheckedChange = {})
                }
            )
            HorizontalDivider()
            SettingRowItem(
                icon = R.drawable.ic_volume,
                label = "Beep Sound on Scan",
                trailingAttribute = {
                    Switch(checked = true, onCheckedChange = {})
                }
            )
            HorizontalDivider()
            SettingRowItem(
                icon = R.drawable.ic_copy,
                label = "Auto Copy Scan Result to Clipboard",
                trailingAttribute = {
                    Switch(checked = true, onCheckedChange = {})
                }
            )
            HorizontalDivider()
            SettingRowItem(
                icon = R.drawable.ic_browser,
                label = "Auto Open URLs After Scan",
                trailingAttribute = {
                    Switch(checked = true, onCheckedChange = {})
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        // --- History & Storage ---
        SettingsSection(title = "History & Storage") {
            SettingRowItem(icon = R.drawable.ic_delete, label = "Clear All Scan History") {
                Text("Delete")
            }
            HorizontalDivider()
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
            HorizontalDivider()
            SettingRowItem(icon = R.drawable.ic_image, label = "Gallery Permission Status") {
                Text("Granted")
            }
            HorizontalDivider()
            SettingRowItem(icon = R.drawable.ic_privacy, label = "Privacy Policy") {
                Text("Granted")
            }
            HorizontalDivider()
            SettingRowItem(icon = R.drawable.ic_bar_chart, label = "Send Anonymous Usage Data") {
                Switch(checked = true, onCheckedChange = {})
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        // --- App Info & Support ---
        SettingsSection(title = "App Info & Support") {
            SettingRowItem(icon = R.drawable.ic_info, label = "App Version and Build Info") {
                Text("v1.0.0(100)")
            }
            HorizontalDivider()
            SettingRowItem(icon = R.drawable.ic_help, label = "About the App") {}
            HorizontalDivider()
            SettingRowItem(icon = R.drawable.ic_rate, label = "Rate this App") {}
            HorizontalDivider()
            SettingRowItem(icon = R.drawable.ic_share, label = "Share This App") {}
        }

        Spacer(modifier = Modifier.height(24.dp)) // final spacing
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