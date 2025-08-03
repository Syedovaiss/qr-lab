package com.ovais.quickcode.features.code_details.presentation

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovais.quickcode.features.code_details.domain.ImageFormatUseCase
import com.ovais.quickcode.features.code_details.domain.SaveImageResult
import com.ovais.quickcode.utils.usecase.ContentResult
import com.ovais.quickcode.utils.usecase.GetContentUriUseCase
import com.ovais.quickcode.utils.usecase.SaveImageUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class BarcodeDetailsViewModel(
    private val saveImageUseCase: SaveImageUseCase,
    private val imageFormatUseCase: ImageFormatUseCase,
    private val getContentUriUseCase: GetContentUriUseCase
) : ViewModel() {


    private val _uiState = MutableStateFlow<BarcodeUiState>(BarcodeUiState.Idle)
    val uiState: StateFlow<BarcodeUiState> = _uiState
    private val _canStartSharing by lazy { MutableSharedFlow<Pair<Boolean, Uri>>() }
    val canStartSharing: SharedFlow<Pair<Boolean, Uri>>
        get() = _canStartSharing.asSharedFlow()

    fun saveImageToGallery(bitmap: Bitmap?, uri: Uri) {
        if (bitmap == null || bitmap.isRecycled) {
            _uiState.value = BarcodeUiState.Error("Invalid bitmap")
            return
        }
        viewModelScope.launch {
            val imageFormat = imageFormatUseCase()
            when (val result = saveImageUseCase(Triple(bitmap, uri, imageFormat))) {
                is SaveImageResult.Saved -> {
                    _uiState.value = BarcodeUiState.ImageSaved
                }

                is SaveImageResult.Failure -> {
                    _uiState.value = BarcodeUiState.Error(result.message)
                }
            }
        }
    }

    fun shareBarcode(bitmap: Bitmap?) {
        if (bitmap == null || bitmap.isRecycled) {
            _uiState.value = BarcodeUiState.Error("Invalid bitmap")
            return
        }
        viewModelScope.launch {
            when (val result = getContentUriUseCase(bitmap)) {
                is ContentResult.Success -> {
                    _canStartSharing.emit(Pair(true, result.uri))
                }

                is ContentResult.Failure -> {
                    _uiState.value = BarcodeUiState.Error(result.message)
                }
            }
        }
    }
}