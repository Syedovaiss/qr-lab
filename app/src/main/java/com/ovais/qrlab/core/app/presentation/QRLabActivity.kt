package com.ovais.qrlab.core.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.ovais.qrlab.core.ui.theme.QrlabTheme
import com.ovais.qrlab.navigation.QRNavigation

class QRLabActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QrlabTheme {
                val snackBarHostState = remember { SnackbarHostState() }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackBarHostState) }
                ) { innerPadding ->
                    QRNavigation(
                        innerPadding,
                        snackBarHostState
                    )
                }
            }
        }
    }
}