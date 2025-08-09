package com.ovais.quickcode.core.app.presentation

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.ovais.quickcode.R
import com.ovais.quickcode.core.di.BACKGROUND
import com.ovais.quickcode.core.ui.theme.QuickCodeTheme
import com.ovais.quickcode.features.splash.domain.DefaultUpdateLocaleUseCase
import com.ovais.quickcode.locale.AppLocaleManager
import com.ovais.quickcode.navigation.QuickCodeNavigation
import com.ovais.quickcode.storage.db.ConfigurationDao
import com.ovais.quickcode.utils.WorkConstants.CSV_WORKER_TAG
import com.ovais.quickcode.utils.WorkConstants.ERROR_KEY
import com.ovais.quickcode.utils.WorkConstants.PDF_WORKER_TAG
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.getKoin
import org.koin.core.qualifier.named

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
            DefaultUpdateLocaleUseCase(
                appLocaleManager = getKoin().get<AppLocaleManager>(),
                configurationDao = getKoin().get<ConfigurationDao>(),
                dispatcherIO = getKoin().get<CoroutineDispatcher>(named(BACKGROUND)),
            ).invoke()
        }
        super.attachBaseContext(context)
    }

    override fun onResume() {
        super.onResume()
        observeWorkRequest()
    }

    private fun observeWorkRequest() {
        observePDFWorker()
        observeCSVWorker()
    }

    private fun observeCSVWorker() {
        WorkManager.getInstance(this)
            .getWorkInfosByTagLiveData(CSV_WORKER_TAG)
            .observe(this) { workInfos ->
                val workInfo = workInfos.firstOrNull()
                workInfo?.let {
                    when (it.state) {
                        WorkInfo.State.SUCCEEDED -> Unit
                        WorkInfo.State.FAILED -> {
                            val error = it.outputData.getString(ERROR_KEY)
                            showToast(error ?: getString(R.string.export_failed))
                        }

                        else -> Unit
                    }
                }
            }
    }

    private fun observePDFWorker() {
        WorkManager.getInstance(this)
            .getWorkInfosByTagLiveData(PDF_WORKER_TAG)
            .observe(this) { workInfos ->
                val workInfo = workInfos.firstOrNull()
                workInfo?.let {
                    when (it.state) {
                        WorkInfo.State.SUCCEEDED -> Unit
                        WorkInfo.State.FAILED -> {
                            val error = it.outputData.getString(ERROR_KEY)
                            showToast(error ?: getString(R.string.export_failed))
                        }

                        else -> Unit
                    }
                }
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG
        ).show()
    }
}