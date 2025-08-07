package com.ovais.quickcode.features.history.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ovais.quickcode.R
import com.ovais.quickcode.utils.components.BodyText
import com.ovais.quickcode.utils.components.ComposableLottieAnimation


@Composable
fun HistoryEmptyView() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ComposableLottieAnimation(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 16.dp, end = 16.dp, start = 16.dp),
            resId = R.raw.empty
        )

        BodyText(
            text = "No created codes yet.\nCreate your first QR code or barcode!"
        )
    }
}