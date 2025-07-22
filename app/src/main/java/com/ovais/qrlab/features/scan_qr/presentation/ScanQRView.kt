package com.ovais.qrlab.features.scan_qr.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.ovais.qrlab.R
import com.ovais.qrlab.utils.components.PermissionRationaleDialog
import com.ovais.qrlab.utils.openAppSettings
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScanQRView(
    scaffoldPaddingValues: PaddingValues,
    viewModel: ScanViewModel = koinViewModel()
) {
    val context = LocalContext.current
    var arePermissionsDenied by remember { mutableStateOf(false) }
    var canShowPreview by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultMap ->
        val allGranted = resultMap.all { it.value }
        if (allGranted) {
            canShowPreview = true
        } else {
            arePermissionsDenied = true
        }
    }

    LaunchedEffect(Unit) {
        viewModel.checkPermissions()
    }
    LaunchedEffect(Unit) {
        viewModel.permissionArray.collectLatest { permissions ->
            if (permissions.isNotEmpty()) {
                permissionLauncher.launch(permissions.toTypedArray())
            } else {
                canShowPreview = true
            }
        }
    }

    Column(
        modifier = Modifier.padding(scaffoldPaddingValues)
    ) {
        if (canShowPreview) {
            BarcodeScannerView {
                viewModel.onImageProxy(it)
            }
        }
        if (arePermissionsDenied) {
            PermissionRationaleDialog(
                title = R.string.permission_denied_title,
                message = R.string.permission_denied_message,
                confirmButtonText = R.string.grant,
                onDismiss = {
                    arePermissionsDenied = true
                },
                onConfirmButtonClicked = {
                    context.openAppSettings()
                }
            )
        }
    }
}

@Composable
fun BarcodeScannerView(
    imageProxy: (ImageProxy) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val previewView = remember { PreviewView(context) }

    AndroidView(
        factory = { previewView },
        modifier = Modifier.fillMaxSize()
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.surfaceProvider = previewView.surfaceProvider
                }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                        imageProxy(imageProxy)
                    }
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalyzer
            )
        }, ContextCompat.getMainExecutor(context))
    }
}