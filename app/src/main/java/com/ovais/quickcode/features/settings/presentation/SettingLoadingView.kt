package com.ovais.quickcode.features.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ovais.quickcode.utils.components.ShimmerCircle
import com.ovais.quickcode.utils.components.ShimmerLine

@Composable
fun SettingLoadingView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {

        repeat(15) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Column {
                    ShimmerLine(
                        width = 150.dp,
                        height = 20.dp
                    )
                    Spacer(Modifier.padding(vertical = 16.dp))
                    ShimmerLine(
                        width = 100.dp,
                        height = 15.dp
                    )
                }
                Spacer(Modifier.weight(1f))
                ShimmerCircle()
            }
        }
    }

}