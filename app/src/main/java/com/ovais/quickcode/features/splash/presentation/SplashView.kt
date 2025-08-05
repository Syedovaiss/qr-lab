package com.ovais.quickcode.features.splash.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ovais.quickcode.R
import com.ovais.quickcode.core.ui.theme.ButtonColor
import com.ovais.quickcode.navigation.Routes
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashView(
    viewModel: SplashViewModel = koinViewModel(),
    onNavigate: (Routes) -> Unit
) {
    val startDestination by viewModel.startDestination.collectAsStateWithLifecycle()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ButtonColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_create_qr),
            contentDescription = "Quick Code Logo",
            modifier = Modifier.size(120.dp),
            tint = Color.Unspecified
        )
    }
    startDestination?.let {
        onNavigate(it)
    }
}