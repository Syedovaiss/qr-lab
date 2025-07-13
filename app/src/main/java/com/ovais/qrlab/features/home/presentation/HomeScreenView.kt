package com.ovais.qrlab.features.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ovais.qrlab.R
import com.ovais.qrlab.utils.components.GradientIconCard
import com.ovais.qrlab.utils.components.HeadingText
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreenView(
    scaffoldPadding: PaddingValues = PaddingValues(),
    viewModel: HomeViewModel = koinViewModel(),
    onClick: (HomeIntent) -> Unit
) {
    val context = LocalContext.current
    val cardItems by viewModel.cardItems.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.nextDestination.collectLatest { intent ->
            onClick(intent)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(scaffoldPadding)
    ) {
        HeadingText(
            text = context.getString(R.string.home_screen_title)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(cardItems) { item ->
                GradientIconCard(
                    modifier = Modifier
                        .width(160.dp)
                        .fillMaxHeight(),
                    gradientColors = item.gradientColors,
                    iconContent = {
                        Image(
                            painter = painterResource(item.iconRes),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .size(48.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    },
                    text = context.getString(item.title),
                    textColor = Color.White,
                    onClick = {
                        viewModel.onAction(item.type)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreenView {

    }
}