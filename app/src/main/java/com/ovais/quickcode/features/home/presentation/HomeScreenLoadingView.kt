package com.ovais.quickcode.features.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ovais.quickcode.core.ui.theme.appBackground
import com.ovais.quickcode.utils.components.ShimmerCardPlaceholder
import com.ovais.quickcode.utils.components.ShimmerCircle
import com.ovais.quickcode.utils.components.ShimmerLine


@Composable
fun HomeScreenLoadingView() {
    Column(
        Modifier
            .padding(16.dp)
            .background(appBackground)
    ) {
        Row(
            Modifier.padding(vertical = 60.dp)
        ) {
            ShimmerCircle(
                size = 64.dp
            )
            Column {
                ShimmerLine(
                    width = 150.dp,
                    height = 20.dp
                )
                Spacer(Modifier.padding(vertical = 4.dp))
                ShimmerLine(
                    width = 100.dp,
                    height = 20.dp
                )
            }
        }
        ShimmerLine(
            width = 100.dp,
            height = 30.dp
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(4) {
                ShimmerCardPlaceholder(
                    modifier = Modifier
                        .width(160.dp)
                        .height(160.dp)
                )
            }
        }
    }
}