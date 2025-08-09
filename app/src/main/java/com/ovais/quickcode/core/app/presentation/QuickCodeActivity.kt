package com.ovais.quickcode.core.app.presentation

import android.content.Context
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
import com.ovais.quickcode.core.di.BACKGROUND
import com.ovais.quickcode.core.ui.theme.QuickCodeTheme
import com.ovais.quickcode.features.splash.domain.DefaultUpdateLocaleUseCase
import com.ovais.quickcode.locale.AppLocaleManager
import com.ovais.quickcode.navigation.QuickCodeNavigation
import com.ovais.quickcode.storage.db.ConfigurationDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.getKoin
import org.koin.core.qualifier.named
import timber.log.Timber

class QuickCodeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuickCodeTheme {
                val snackBarHostState = remember { SnackbarHostState() }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackBarHostState) }
                ) { innerPadding ->
                    QuickCodeNavigation(
                        innerPadding,
                        snackBarHostState
                    )
                }
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val context = runBlocking {
            try {
                DefaultUpdateLocaleUseCase(
                    appLocaleManager = getKoin().get<AppLocaleManager>(),
                    configurationDao = getKoin().get<ConfigurationDao>(),
                    dispatcherIO = getKoin().get<CoroutineDispatcher>(named(BACKGROUND)),
                ).invoke()
            } catch (e: Exception) {
                Timber.e(e)
                newBase
            }
        }
        super.attachBaseContext(context ?: newBase)
    }
}