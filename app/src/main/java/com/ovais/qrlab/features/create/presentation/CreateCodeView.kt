package com.ovais.qrlab.features.create.presentation

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ovais.qrlab.R
import com.ovais.qrlab.features.create.data.CodeFormats
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
        CodeTypeFormScreen(
            codeItems,
            onCreateCode = { selectedValues, type ->
                viewModel.createCode(selectedValues, selectedType, type)
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