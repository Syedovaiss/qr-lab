package com.ovais.qrlab.features.scan_qr.presentation

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ovais.qrlab.R
import com.ovais.qrlab.utils.components.PermissionRationaleDialog
import com.ovais.qrlab.utils.openAppSettings
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScanQRView(
    scaffoldPaddingValues: PaddingValues,
    viewModel: ScanViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState
) {
    val scanResult by viewModel.scanResult.collectAsStateWithLifecycle()
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
        viewModel.errorMessage.collectLatest {
            snackbarHostState.showSnackbar(it)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.checkPermissions()
    }
    LaunchedEffect(scanResult) {
        scanResult?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
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
            BarcodeScannerView(
                imageProxy = { image ->
                    viewModel.onImageProxy(image)
                },
                onGalleryImagePicked = { uri ->
                    viewModel.onImagePickedFromGallery(uri)
                }
            )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarcodeScannerView(
    imageProxy: (ImageProxy) -> Unit,
    onGalleryImagePicked: (Uri) -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }

    var cameraControl by remember { mutableStateOf<CameraControl?>(null) }
    var isTorchOn by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { onGalleryImagePicked(it) }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        ) {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().also {
                    it.surfaceProvider = previewView.surfaceProvider
                }

                val imageAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(ContextCompat.getMainExecutor(context)) { image ->
                            imageProxy(image)
                        }
                    }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                cameraProvider.unbindAll()
                val camera = cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalyzer
                )

                cameraControl = camera.cameraControl
            }, ContextCompat.getMainExecutor(context))
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val galleryInteractionSource = remember { MutableInteractionSource() }
            val torchMutableInteractionSource = remember { MutableInteractionSource() }
            Image(
                painter = painterResource(R.drawable.ic_gallery_add_white),
                null,
                modifier = Modifier
                    .size(32.dp)
                    .clickable(
                        galleryInteractionSource,
                        LocalIndication.current
                    ) {
                        galleryLauncher.launch("image/*")
                    }

            )
            VerticalDivider(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .size(20.dp)
            )
            Image(
                painter = if (isTorchOn) painterResource(R.drawable.ic_flash_on) else painterResource(
                    R.drawable.ic_flash_off
                ),
                null,
                modifier = Modifier
                    .size(32.dp)
                    .clickable(
                        torchMutableInteractionSource,
                        LocalIndication.current
                    ) {
                        isTorchOn = !isTorchOn
                        cameraControl?.enableTorch(isTorchOn)
                    }

            )
        }
    }
}