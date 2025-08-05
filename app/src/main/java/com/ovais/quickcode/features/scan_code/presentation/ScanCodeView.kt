package com.ovais.quickcode.features.scan_code.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ovais.quickcode.R
import com.ovais.quickcode.core.ui.theme.ColorPrimary
import com.ovais.quickcode.core.ui.theme.ColorSecondary
import com.ovais.quickcode.utils.components.PermissionRationaleDialog
import com.ovais.quickcode.utils.isURL
import com.ovais.quickcode.utils.openAppSettings
import com.ovais.quickcode.utils.openURL
import com.ovais.quickcode.utils.orEmpty
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun ScanQRView(
    scaffoldPaddingValues: PaddingValues,
    snackBarHostState: SnackbarHostState,
    viewModel: ScanViewModel = koinViewModel(),
    onBack: () -> Unit
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
    var showDialog by remember { mutableStateOf(true) }
    val canAutoCopyToClipboard by viewModel.canAutoCopyToClipboard.collectAsStateWithLifecycle()
    val canAutoOpenURL by viewModel.canAutoOpenURL.collectAsStateWithLifecycle()
    var canShowCopiedSnackBar by remember { mutableStateOf(false) }
    val copiedText = stringResource(R.string.copied_to_clipboard)

    LaunchedEffect(canShowCopiedSnackBar) {
        if (canShowCopiedSnackBar) {
            snackBarHostState.showSnackbar(copiedText)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.checkPermissions()
    }
    LaunchedEffect(scanResult) {
        scanResult?.let {
            if (it.isNotEmpty()) {
                showDialog = true
            }
        }
    }
    if (showDialog) {
        val label = stringResource(R.string.clipboard_label)
        ScanResultDialog(
            resultText = scanResult.orEmpty,
            copy = {
                viewModel.copyToClipboard(label, scanResult.orEmpty)
            },
            onDismiss = { showDialog = false }
        )
        if (canAutoCopyToClipboard) {
            viewModel.copyToClipboard(label, scanResult.orEmpty)
        }
        if (canAutoOpenURL && scanResult.orEmpty.isURL) {
            context.openURL(scanResult.orEmpty)
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
            CodeScannerView(
                imageProxy = { image ->
                    viewModel.onImageProxy(image)
                },
                onGalleryImagePicked = { uri ->
                    viewModel.onImagePickedFromGallery(uri)
                },
                onBack = {
                    showDialog = false
                    onBack()
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

@Composable
fun CodeScannerView(
    imageProxy: (ImageProxy) -> Unit,
    onGalleryImagePicked: (Uri) -> Unit = {},
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }

    var cameraControl by remember { mutableStateOf<CameraControl?>(null) }
    var isTorchOn by remember { mutableStateOf(false) }
    var zoomLevel by remember { mutableFloatStateOf(1f) }
    var showHelpDialog by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { onGalleryImagePicked(it) }
    }

    // Initialize zoom when camera is ready
    LaunchedEffect(cameraControl) {
        cameraControl?.let { control ->
            try {
                control.setZoomRatio(1f)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Camera Preview
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

        // Overlay with scanning area
        Box(modifier = Modifier.fillMaxSize()) {
            // Semi-transparent overlay
            Canvas(modifier = Modifier.fillMaxSize()) {
                val screenWidth = size.width
                val screenHeight = size.height
                val scanAreaSize = 500f
                val scanAreaX = (screenWidth - scanAreaSize) / 2
                val scanAreaY = (screenHeight - scanAreaSize) / 2

                val overlayPath = Path().apply {
                    fillType = PathFillType.EvenOdd
                    // Start with the full screen
                    addRect(Rect(0f, 0f, screenWidth, screenHeight))
                    // Cut out the scanning area (this creates a hole in the overlay)
                    addRoundRect(
                        RoundRect(
                            left = scanAreaX,
                            top = scanAreaY,
                            right = scanAreaX + scanAreaSize,
                            bottom = scanAreaY + scanAreaSize,
                            cornerRadius = CornerRadius(20f, 20f)
                        )
                    )
                }

                // Draw the overlay with cutout
                drawPath(
                    path = overlayPath,
                    color = Color.Black.copy(alpha = 0.6f)
                )

                // Draw corner indicators with rounded corners
                val cornerLength = 40f
                val cornerThickness = 8f
                val cornerRadius = 12f

                // Top-left corner
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(scanAreaX, scanAreaY),
                    size = Size(cornerLength, cornerThickness),
                    cornerRadius = CornerRadius(
                        cornerRadius,
                        cornerRadius
                    )
                )
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(scanAreaX, scanAreaY),
                    size = Size(cornerThickness, cornerLength),
                    cornerRadius = CornerRadius(
                        cornerRadius,
                        cornerRadius
                    )
                )

                // Top-right corner
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(scanAreaX + scanAreaSize - cornerLength, scanAreaY),
                    size = Size(cornerLength, cornerThickness),
                    cornerRadius = CornerRadius(
                        cornerRadius,
                        cornerRadius
                    )
                )
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(scanAreaX + scanAreaSize - cornerThickness, scanAreaY),
                    size = Size(cornerThickness, cornerLength),
                    cornerRadius = CornerRadius(
                        cornerRadius,
                        cornerRadius
                    )
                )

                // Bottom-left corner
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(scanAreaX, scanAreaY + scanAreaSize - cornerThickness),
                    size = Size(cornerLength, cornerThickness),
                    cornerRadius = CornerRadius(
                        cornerRadius,
                        cornerRadius
                    )
                )
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(scanAreaX, scanAreaY + scanAreaSize - cornerLength),
                    size = Size(cornerThickness, cornerLength),
                    cornerRadius = CornerRadius(
                        cornerRadius,
                        cornerRadius
                    )
                )

                // Bottom-right corner
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(
                        scanAreaX + scanAreaSize - cornerLength,
                        scanAreaY + scanAreaSize - cornerThickness
                    ),
                    size = Size(cornerLength, cornerThickness),
                    cornerRadius = CornerRadius(
                        cornerRadius,
                        cornerRadius
                    )
                )
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(
                        scanAreaX + scanAreaSize - cornerThickness,
                        scanAreaY + scanAreaSize - cornerLength
                    ),
                    size = Size(cornerThickness, cornerLength),
                    cornerRadius = CornerRadius(
                        cornerRadius,
                        cornerRadius
                    )
                )
            }
        }

        // Back button
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 60.dp, start = 20.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(12.dp)
                .clickable(
                    remember { MutableInteractionSource() },
                    LocalIndication.current
                ) {
                    onBack()
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = "Back",
                modifier = Modifier.size(24.dp),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
            )
        }

        // Top controls row
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 60.dp, end = 20.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(25.dp)
                )
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val galleryInteractionSource = remember { MutableInteractionSource() }
            val torchInteractionSource = remember { MutableInteractionSource() }
            val helpInteractionSource = remember { MutableInteractionSource() }

            Image(
                painter = painterResource(R.drawable.ic_gallery_add_white),
                contentDescription = "Gallery",
                modifier = Modifier
                    .size(28.dp)
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
                    .size(20.dp),
                color = Color.White.copy(alpha = 0.5f)
            )

            Image(
                painter = if (isTorchOn) painterResource(R.drawable.ic_flash_on) else painterResource(
                    R.drawable.ic_flash_off
                ),
                contentDescription = "Torch",
                modifier = Modifier
                    .size(28.dp)
                    .clickable(
                        torchInteractionSource,
                        LocalIndication.current
                    ) {
                        isTorchOn = !isTorchOn
                        cameraControl?.enableTorch(isTorchOn)
                    }
            )

            VerticalDivider(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .size(20.dp),
                color = Color.White.copy(alpha = 0.5f)
            )

            Image(
                painter = painterResource(R.drawable.ic_help_white),
                contentDescription = "Help",
                modifier = Modifier
                    .size(28.dp)
                    .clickable(
                        helpInteractionSource,
                        LocalIndication.current
                    ) {
                        showHelpDialog = true
                    }
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(25.dp)
                )
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_zoom_in),
                contentDescription = "Zoom",
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Slider(
                value = zoomLevel,
                onValueChange = { newZoom ->
                    zoomLevel = newZoom
                    try {
                        cameraControl?.setZoomRatio(newZoom)
                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                },
                valueRange = 1f..3f,
                modifier = Modifier.width(200.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.zoom_level, zoomLevel),
                color = Color.White,
                fontSize = 12.sp
            )
        }
        if (showHelpDialog) {
            AlertDialog(
                onDismissRequest = { showHelpDialog = false },
                containerColor = Color.White,
                shape = RoundedCornerShape(20.dp),
                title = {
                    Text(
                        text = stringResource(R.string.scan_help_title),
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                },
                text = {
                    Column(
                        modifier = Modifier.padding(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Steps
                        val steps = listOf(
                            R.string.scan_step_1_title to R.string.scan_step_1_desc,
                            R.string.scan_step_2_title to R.string.scan_step_2_desc,
                            R.string.scan_step_3_title to R.string.scan_step_3_desc,
                            R.string.scan_step_4_title to R.string.scan_step_4_desc
                        )

                        steps.forEachIndexed { index, (titleRes, descRes) ->
                            Row(
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(
                                            color = ColorPrimary,
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${index + 1}",
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                }
                                Column {
                                    Text(
                                        text = stringResource(titleRes),
                                        fontSize = 14.sp,
                                        color = Color.Black
                                    )
                                    Text(
                                        text = stringResource(descRes),
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }

                        // Tips section
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = ColorSecondary.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(16.dp)
                        ) {
                            Column {
                                Text(
                                    text = stringResource(R.string.scan_tips_title),
                                    fontSize = 14.sp,
                                    color = ColorPrimary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = stringResource(R.string.scan_tips_bullets),
                                    fontSize = 12.sp,
                                    color = ColorPrimary
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    (TextButton(onClick = { showHelpDialog = false }) {
                        Text(
                            text = stringResource(R.string.got_it),
                            color = ColorPrimary
                        )
                    })
                }
            )
        }
    }
}