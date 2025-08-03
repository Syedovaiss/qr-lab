package com.ovais.quickcode.features.code_details.presentation

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovais.quickcode.analytics.AppAnalyticsManager
import com.ovais.quickcode.features.code_details.domain.ImageFormatUseCase
import com.ovais.quickcode.features.code_details.domain.SaveImageResult
import com.ovais.quickcode.utils.AnalyticsConstant.DATA
import com.ovais.quickcode.utils.AnalyticsConstant.ERROR
import com.ovais.quickcode.utils.AnalyticsConstant.ERROR_REASON
import com.ovais.quickcode.utils.AnalyticsConstant.IMAGE_SAVED
import com.ovais.quickcode.utils.AnalyticsConstant.IMAGE_SHARED
import com.ovais.quickcode.utils.AnalyticsConstant.SAVE_IMAGE
import com.ovais.quickcode.utils.AnalyticsConstant.SHARE_IMAGE
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
    private val getContentUriUseCase: GetContentUriUseCase,
    private val analyticsManager: AppAnalyticsManager
) : ViewModel() {


    private val _uiState = MutableStateFlow<BarcodeUiState>(BarcodeUiState.Idle)
    val uiState: StateFlow<BarcodeUiState> = _uiState
    private val _canStartSharing by lazy { MutableSharedFlow<Pair<Boolean, Uri>>() }
    val canStartSharing: SharedFlow<Pair<Boolean, Uri>>
        get() = _canStartSharing.asSharedFlow()

    fun saveImageToGallery(bitmap: Bitmap?, uri: Uri) {
        if (bitmap == null || bitmap.isRecycled) {
            analyticsManager.logEvent(
                SAVE_IMAGE,
                ERROR,
                hashMapOf(ERROR_REASON to "Invalid Bitmap")
            )
            _uiState.value = BarcodeUiState.Error("Invalid bitmap")
            return
        }
        viewModelScope.launch {
            val imageFormat = imageFormatUseCase()
            when (val result = saveImageUseCase(Triple(bitmap, uri, imageFormat))) {
                is SaveImageResult.Saved -> {
                    analyticsManager.logEvent(
                        SAVE_IMAGE,
                        IMAGE_SAVED,
                        hashMapOf(DATA to imageFormat)
                    )
                    _uiState.value = BarcodeUiState.ImageSaved
                }

                is SaveImageResult.Failure -> {
                    analyticsManager.logEvent(
                        SAVE_IMAGE,
                        ERROR,
                        hashMapOf(ERROR_REASON to result.message)
                    )
                    _uiState.value = BarcodeUiState.Error(result.message)
                }
            }
        }
    }

    fun shareBarcode(bitmap: Bitmap?) {
        if (bitmap == null || bitmap.isRecycled) {
            analyticsManager.logEvent(
                SHARE_IMAGE,
                ERROR,
                hashMapOf(ERROR_REASON to "Bitmap is null or recycled!")
            )
            _uiState.value = BarcodeUiState.Error("Invalid bitmap")
            return
        }
        viewModelScope.launch {
            when (val result = getContentUriUseCase(bitmap)) {
                is ContentResult.Success -> {
                    analyticsManager.logEvent(
                        SHARE_IMAGE,
                        IMAGE_SHARED,
                        hashMapOf(DATA to bitmap)
                    )
                    _canStartSharing.emit(Pair(true, result.uri))
                }

                is ContentResult.Failure -> {
                    analyticsManager.logEvent(
                        SHARE_IMAGE,
                        ERROR,
                        hashMapOf(ERROR_REASON to result.message)
                    )
                    _uiState.value = BarcodeUiState.Error(result.message)
                }
            }
        }
    }
}