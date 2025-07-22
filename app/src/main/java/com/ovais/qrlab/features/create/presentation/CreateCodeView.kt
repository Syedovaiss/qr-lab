package com.ovais.qrlab.features.create.presentation

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ovais.qrlab.R
import com.ovais.qrlab.features.create.data.CodeFormats
import com.ovais.qrlab.utils.components.HeadingText
import com.ovais.qrlab.utils.components.ImagePicker
import com.ovais.qrlab.utils.components.PermissionRationaleDialog
import com.ovais.qrlab.utils.components.SubtitleText
import com.ovais.qrlab.utils.openAppSettings
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateQRView(
    scaffoldPaddingValues: PaddingValues,
    viewModel: CreateCodeViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val codeItems by viewModel.codeItems.collectAsStateWithLifecycle()
    val codeFormats by viewModel.codeFormats.collectAsStateWithLifecycle()
    var selectedType by remember { mutableStateOf<CodeFormats?>(null) }
    var showGeneratedCode by remember { mutableStateOf(false) }
    var generatedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var backgroundColor by remember { mutableStateOf(Color.White) }
    var foregroundColor by remember { mutableStateOf(Color.Black) }
    var selectedLogo by remember { mutableStateOf<Bitmap?>(null) }
    var canShowImagePicker by remember { mutableStateOf(false) }
    var arePermissionsDenied by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultMap ->
        val allGranted = resultMap.all { it.value }
        if (allGranted) {
            canShowImagePicker = true
        } else {
            arePermissionsDenied = true
            canShowImagePicker = false
        }
    }
    LaunchedEffect(Unit) {
        viewModel.errorMessage.collectLatest {
            snackbarHostState.showSnackbar(it)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.permissionArray.collectLatest { permissions ->
            if (permissions.isNotEmpty()) {
                permissionLauncher.launch(permissions.toTypedArray())
            } else {
                canShowImagePicker = true
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.code.collectLatest { bitmap ->
            bitmap?.let {
                generatedBitmap = it
                showGeneratedCode = true
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(scaffoldPaddingValues)
            .verticalScroll(scrollState)
    ) {

        HeadingText(
            stringResource(R.string.create_new),
            paddingValues = PaddingValues(16.dp)
        )
        SubtitleText(
            stringResource(R.string.code_format),
            paddingValues = PaddingValues(16.dp)
        )
        CodeFormatDropDown(codeFormats, selectedType) {
            selectedType = it
            viewModel.onCodeSelection(it)
        }
        if (selectedType is CodeFormats.QRCode) {
            BackgroundColorPicker {
                backgroundColor = it
            }
            ForegroundColorPicker {
                foregroundColor = it
            }
            if (canShowImagePicker) {
                SubtitleText(stringResource(R.string.upload_logo))
                ImagePicker(viewModel.fileManagerImpl) {
                    selectedLogo = it
                }
                selectedLogo?.let { logo ->
                    SubtitleText(stringResource(R.string.image))
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .size(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.LightGray)
                    ) {
                        Image(
                            bitmap = logo.asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

        }
        CodeTypeFormScreen(
            codeItems,
            onCreateCode = { selectedValues, type ->
                val colorPair = Pair(backgroundColor, foregroundColor)
                viewModel.createCode(selectedValues, selectedType, type, colorPair, selectedLogo)
            }
        )
        BarcodeViewDialog(
            show = showGeneratedCode,
            bitmap = generatedBitmap
        ) {
            showGeneratedCode = false
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