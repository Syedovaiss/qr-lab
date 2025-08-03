package com.ovais.quickcode.features.history.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ovais.quickcode.R
import com.ovais.quickcode.utils.components.TopBar

@Composable
fun HistoryScreen(
    onBack: () -> Unit
) {
    TopBar(
        title = R.string.history,
        onBack
    )
}