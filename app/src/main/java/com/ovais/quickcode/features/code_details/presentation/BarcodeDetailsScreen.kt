package com.ovais.quickcode.features.code_details.presentation

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ovais.quickcode.R
import com.ovais.quickcode.core.ui.theme.appBackground
import com.ovais.quickcode.utils.AnalyticsConstant.BITMAP_CONTENT
import com.ovais.quickcode.utils.AnalyticsConstant.DATA
import com.ovais.quickcode.utils.AnalyticsConstant.ERROR
import com.ovais.quickcode.utils.AnalyticsConstant.ERROR_REASON
import com.ovais.quickcode.utils.AnalyticsConstant.VIEW_SCANNED_CODE_CONTENT
import com.ovais.quickcode.utils.AnalyticsConstant.VIEW_SCANNED_CODE_EVENT
import com.ovais.quickcode.utils.AnalyticsConstant.VIEW_SCANNED_CODE_IMAGE
import com.ovais.quickcode.utils.AnalyticsConstant.VIEW_SCANNED_IMAGE_EVENT
import com.ovais.quickcode.utils.components.PrimaryButton
import com.ovais.quickcode.utils.components.SubtitleText
import com.ovais.quickcode.utils.components.TopBar
import com.ovais.quickcode.utils.shareIntent
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun BarcodeDetailsScreen(
    data: Pair<Bitmap?, MutableMap<String, String>>,
    onBack: () -> Unit,
    viewModel: BarcodeDetailsViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val (bitmap, contentMap) = data

    if (bitmap == null || bitmap.isRecycled) {
        BarcodeErrorView(onError = {
            viewModel.logEvent(
                BITMAP_CONTENT,
                ERROR,
                hashMapOf(ERROR_REASON to "Bitmap is null or recycled!")
            )
        },onBack)
        return
    }

    val scrollState = rememberScrollState()
    var showSaveDialog by remember { mutableStateOf(false) }
    var showSuccessMessage by remember { mutableStateOf(false) }
    var showErrorMessage by remember { mutableStateOf(false) }

    val saveLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("image/png")
    ) { uri ->
        uri?.let {
            viewModel.saveImageToGallery(bitmap, uri)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.canStartSharing.collect { result ->
            if (result.first) {
                context.shareIntent(result.second)
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.uiState.collectLatest { state ->
            when (state) {
                is BarcodeUiState.ImageSaved -> {
                    showSuccessMessage = true
                }

                is BarcodeUiState.Error -> {
                    showErrorMessage = true
                }

                else -> Unit
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(appBackground)
            .verticalScroll(scrollState)
    ) {
        // Header with back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TopBar(
                title = R.string.barcode_details,
                onBack = onBack
            )
        }

        // Content Details Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                SubtitleText(
                    text = stringResource(R.string.content_details),
                    paddingValues = PaddingValues(bottom = 16.dp)
                )
                viewModel.logEvent(
                    VIEW_SCANNED_CODE_EVENT,
                    VIEW_SCANNED_CODE_CONTENT,
                    hashMapOf(DATA to contentMap)
                )
                contentMap.forEach { (key, value) ->
                    if (value.isNotBlank()) {
                        ContentDetailItem(
                            label = key.replaceFirstChar { it.uppercase() },
                            value = value
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }

        // Barcode Image Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SubtitleText(
                    text = stringResource(R.string.generated_barcode),
                    paddingValues = PaddingValues(bottom = 16.dp)
                )

                Box(
                    modifier = Modifier
                        .size(280.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    viewModel.logEvent(
                        VIEW_SCANNED_IMAGE_EVENT,
                        VIEW_SCANNED_CODE_IMAGE
                    )

                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = stringResource(R.string.barcode_image),
                        modifier = Modifier
                            .size(260.dp)
                            .padding(8.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PrimaryButton(
                        title = R.string.save_image,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(vertical = 8.dp)
                    ) {
                        showSaveDialog = true
                    }
                    PrimaryButton(
                        title = R.string.share,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        viewModel.shareBarcode(bitmap)
                    }
                }
            }
        }
    }

    // Save Dialog
    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text(stringResource(R.string.save_barcode)) },
            text = { Text(stringResource(R.string.save_barcode_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSaveDialog = false
                        if (!bitmap.isRecycled) {
                            saveLauncher.launch("barcode_${System.currentTimeMillis()}.png")
                        }
                    }
                ) {
                    Text(stringResource(R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    // Success Message
    if (showSuccessMessage) {
        AlertDialog(
            onDismissRequest = { showSuccessMessage = false },
            title = { Text(stringResource(R.string.success)) },
            text = { Text(stringResource(R.string.image_saved_successfully)) },
            confirmButton = {
                TextButton(onClick = { showSuccessMessage = false }) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }

    if (showErrorMessage) {
        AlertDialog(
            onDismissRequest = { showErrorMessage = false },
            title = { Text(stringResource(R.string.error)) },
            text = { Text(stringResource(R.string.failed_to_save_image)) },
            confirmButton = {
                TextButton(onClick = { showErrorMessage = false }) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }
}

@Composable
private fun ContentDetailItem(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier.padding(12.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Start
        )
    }
}