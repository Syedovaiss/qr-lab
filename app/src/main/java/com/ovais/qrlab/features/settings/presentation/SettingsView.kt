package com.ovais.qrlab.features.settings.presentation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ovais.qrlab.R
import com.ovais.qrlab.utils.components.HeadingText
import com.ovais.qrlab.utils.components.SubtitleText

@Composable
fun SettingsView(scaffoldPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(scaffoldPadding)
    ) {
        HeadingText(
            stringResource(R.string.settings),
            paddingValues = PaddingValues(top = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        // --- Customization Section ---
        SettingsSection(title = stringResource(R.string.customization)) {
            // App Theme
            SettingsDropdownItem(
                icon = R.drawable.ic_palette,
                label = "App Theme",
                options = listOf("System", "Light", "Dark"),
                selectedOption = "System",
                onOptionSelected = {}
            )
            HorizontalDivider()
            // Default QR Code Color
            SettingsColorItem(
                icon = R.drawable.ic_color,
                label = "Default QR Code Color",
                color = Color(0xFF222222),
                onClick = {}
            )
            HorizontalDivider()
            // Default QR Code Background
            SettingsColorItem(
                icon = R.drawable.ic_color_fill,
                label = "Default QR Code Background",
                color = Color(0xFFFFFFFF),
                onClick = {}
            )
            HorizontalDivider()
            // Default Logo on QR Code
            SettingsImageItem(
                icon = R.drawable.ic_image,
                label = "Default Logo on QR Code",
                onClick = {}
            )
            HorizontalDivider()
            // Default QR Export Format
            SettingsDropdownItem(
                icon = R.drawable.ic_save,
                label = "Default QR Export Format",
                options = listOf("PNG", "SVG"),
                selectedOption = "PNG",
                onOptionSelected = {}
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        // --- User Feedback & Behavior ---
        SettingsSection(title = "User Feedback & Behavior") {
            SettingsSwitchItem(
                icon = R.drawable.ic_vibration,
                label = "Vibration on Scan",
                checked = true,
                onCheckedChange = {}
            )
            HorizontalDivider()
            SettingsSwitchItem(
                icon = R.drawable.ic_volume,
                label = "Beep Sound on Scan",
                checked = true,
                onCheckedChange = {}
            )
            HorizontalDivider()
            SettingsSwitchItem(
                icon = R.drawable.ic_copy,
                label = "Auto Copy Scan Result to Clipboard",
                checked = false,
                onCheckedChange = {}
            )
            HorizontalDivider()
            SettingsSwitchItem(
                icon = R.drawable.ic_browser,
                label = "Auto Open URLs After Scan",
                checked = false,
                onCheckedChange = {}
            )
            HorizontalDivider()
            SettingsSwitchItem(
                icon = R.drawable.ic_history,
                label = "Auto Save Scan History",
                checked = true,
                onCheckedChange = {}
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        // --- History & Storage ---
        SettingsSection(title = "History & Storage") {
            SettingsActionItem(
                icon = R.drawable.ic_delete,
                label = "Clear All Scan History",
                onClick = {}
            )
            HorizontalDivider()
            SettingsDropdownItem(
                icon = R.drawable.ic_download,
                label = "Export History",
                options = listOf("CSV", "JSON"),
                selectedOption = "CSV",
                onOptionSelected = {}
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        // --- Language & Regional ---
        SettingsSection(title = "Language & Regional") {
            SettingsDropdownItem(
                icon = R.drawable.ic_lanugage,
                label = "App Language",
                options = listOf("System", "English", "Urdu"),
                selectedOption = "System",
                onOptionSelected = {}
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        // --- Privacy & Permissions ---
        SettingsSection(title = "Privacy & Permissions") {
            SettingsStatusItem(
                icon = R.drawable.ic_camera,
                label = "Camera Permission Status",
                status = "Granted"
            )
            HorizontalDivider()
            SettingsStatusItem(
                icon = R.drawable.ic_image,
                label = "Gallery Access Permission",
                status = "Granted"
            )
            HorizontalDivider()
            SettingsActionItem(
                icon = R.drawable.ic_privacy,
                label = "View Privacy Policy",
                onClick = {}
            )
            HorizontalDivider()
            SettingsSwitchItem(
                icon = R.drawable.ic_bar_chart,
                label = "Send Anonymous Usage Data",
                checked = false,
                onCheckedChange = {}
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        // --- App Info & Support ---
        SettingsSection(title = "App Info & Support") {
            SettingsStatusItem(
                icon = R.drawable.ic_info,
                label = "App Version and Build Info",
                status = "v1.0.0 (100)"
            )
            HorizontalDivider()
            SettingsActionItem(
                icon = R.drawable.ic_help,
                label = "About the App",
                onClick = {}
            )
            HorizontalDivider()
            SettingsActionItem(
                icon = R.drawable.ic_rate,
                label = "Rate This App",
                onClick = {}
            )
            HorizontalDivider()
            SettingsActionItem(
                icon = R.drawable.ic_share,
                label = "Share This App",
                onClick = {}
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        SubtitleText(
            title,
            paddingValues = PaddingValues(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
        )
        content()
    }
}

@Composable
private fun SettingsSwitchItem(
    @DrawableRes icon: Int,
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(icon),
            label
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun SettingsDropdownItem(
    @DrawableRes icon: Int,
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(selectedOption) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = label,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Text(
                selected,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selected = option
                        expanded = false
                        onOptionSelected(option)
                    }
                )
            }
        }
    }
}

@Composable
private fun SettingsColorItem(
    @DrawableRes icon: Int,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(icon),
            label
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        androidx.compose.foundation.Canvas(modifier = Modifier.size(24.dp)) {
            drawCircle(color = color)
        }
    }
}

@Composable
private fun SettingsImageItem(
    @DrawableRes icon: Int,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(icon),
            label
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Image(
            painterResource(R.drawable.ic_right),
            null
        )
    }
}

@Composable
private fun SettingsActionItem(
    @DrawableRes icon: Int,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(icon),
            label
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Image(
            painterResource(R.drawable.ic_right),
            null
        )
    }
}

@Composable
private fun SettingsStatusItem(
    @DrawableRes icon: Int,
    label: String,
    status: String
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(icon),
            label
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Text(status, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsPreview() {
    SettingsView(PaddingValues(16.dp))
}