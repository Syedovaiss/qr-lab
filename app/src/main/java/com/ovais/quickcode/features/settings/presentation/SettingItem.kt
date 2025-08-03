package com.ovais.quickcode.features.settings.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ovais.quickcode.utils.components.BodyText

@Composable
fun SettingRowItem(
    label: String,
    description: String,
    trailingAttribute: @Composable () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp)
        ) {
            BodyText(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                paddingValues = PaddingValues(bottom = 2.dp)
            )
            BodyText(
                text = description,
                fontSize = 13.sp,
                paddingValues = PaddingValues(),
                color = Color.Gray,
                fontWeight = FontWeight.Normal
            )
        }

        trailingAttribute()
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingRowPreview() {
    SettingRowItem(
        label = "Camera Permission Status",
        description = "Permissions required for scanning..."
    ) {
        Text("Granted")
    }
}