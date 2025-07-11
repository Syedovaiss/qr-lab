package com.ovais.qrlab.features.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovais.qrlab.R
import com.ovais.qrlab.utils.components.HeadingText
import com.ovais.qrlab.utils.components.SubtitleText

@Composable
fun HomeScreenView(
    scaffoldPadding: PaddingValues = PaddingValues(),
    onCreateQR: () -> Unit,
    onScanQR: () -> Unit,
    onSettingsClicked: () -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(scaffoldPadding)
    ) {
        HeadingText(
            text = context.getString(R.string.home_screen_title)
        )

        HomeMenuItem(
            label = context.getString(R.string.create_qr),
            iconRes = R.drawable.ic_create_qr
        ) {
            onCreateQR()
        }

        HomeMenuItem(
            label = context.getString(R.string.scan_qr),
            iconRes = R.drawable.ic_scan_qr
        ) {
            onScanQR()
        }

        HomeMenuItem(
            label = context.getString(R.string.settings),
            iconRes = R.drawable.ic_settings
        ) {
            onSettingsClicked()
        }
    }
}

@Composable
fun HomeMenuItem(
    label: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier = Modifier.clickable(
            interactionSource = interactionSource,
            indication = LocalIndication.current
        ) {
            onClick()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubtitleText(
                text = label,
                modifier = Modifier.weight(1f)
            )
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier
                    .size(24.dp)
            )
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreenView(
        onScanQR = {},
        onCreateQR = {},
        onSettingsClicked = {}
    )
}