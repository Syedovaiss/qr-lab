package com.ovais.quickcode.core.app.presentation

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
import com.ovais.quickcode.core.ui.theme.QuickCodeTheme
import com.ovais.quickcode.navigation.QuickCodeNavigation
import com.ovais.quickcode.utils.WorkConstants.CSV_WORKER_TAG
import com.ovais.quickcode.utils.WorkConstants.ERROR_KEY
import com.ovais.quickcode.utils.WorkConstants.PDF_WORKER_TAG

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