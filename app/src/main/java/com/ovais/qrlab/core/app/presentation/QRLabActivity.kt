package com.ovais.qrlab.core.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.ovais.qrlab.core.ui.theme.QrlabTheme
import com.ovais.qrlab.navigation.QRNavigation

class QRLabActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QrlabTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    QRNavigation(innerPadding)
                }
            }
        }
    }
}