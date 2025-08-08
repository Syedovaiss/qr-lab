package com.ovais.quickcode.features.history.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ovais.quickcode.utils.components.ShimmerCardPlaceholder


@Composable
fun HistoryLoadingView() {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row {
            repeat(2) {
                ShimmerCardPlaceholder(
                    modifier = Modifier
                        .padding(16.dp)
                        .width(100.dp)
                        .height(30.dp)
                )
            }
        }

        repeat(5) {
            ShimmerCardPlaceholder(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        16.dp
                    )
            )
        }

    }

}