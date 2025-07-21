package com.ovais.qrlab.features.create.presentation

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ovais.qrlab.R
import com.ovais.qrlab.core.ui.theme.colorsForColorPicker
import com.ovais.qrlab.features.create.data.CodeFormats
import com.ovais.qrlab.utils.components.ColorPickerDialog
import com.ovais.qrlab.utils.components.ColorPickerGrid
import com.ovais.qrlab.utils.components.HeadingText
import com.ovais.qrlab.utils.components.SubtitleText
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateQRView(
    scaffoldPaddingValues: PaddingValues,
    viewModel: CreateCodeViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState
) {
    val scrollState = rememberScrollState()
    val codeItems by viewModel.codeItems.collectAsStateWithLifecycle()
    val codeFormats by viewModel.codeFormats.collectAsStateWithLifecycle()
    var selectedType by remember { mutableStateOf<CodeFormats?>(null) }
    var showGeneratedCode by remember { mutableStateOf(false) }
    var generatedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var backgroundColor by remember { mutableStateOf(Color.Black) }
    var foregroundColor by remember { mutableStateOf(Color.Black) }
    LaunchedEffect(Unit) {
        viewModel.errorMessage.collectLatest {
            snackbarHostState.showSnackbar(it)
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
        }
        if (selectedType is CodeFormats.QRCode) {
            BackgroundColorPicker {
                backgroundColor = it
            }
            ForegroundColorPicker {
                foregroundColor = it
            }

        }
        CodeTypeFormScreen(
            codeItems,
            onCreateCode = { selectedValues, type ->
                val colorPair = Pair(backgroundColor,foregroundColor)
                viewModel.createCode(selectedValues, selectedType, type, colorPair)
            }
        )
        BarcodeViewDialog(
            show = showGeneratedCode,
            bitmap = generatedBitmap
        ) {
            showGeneratedCode = false
        }

    }
}