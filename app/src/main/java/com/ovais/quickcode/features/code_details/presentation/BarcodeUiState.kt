package com.ovais.quickcode.features.code_details.presentation

sealed interface BarcodeUiState {
    data object Idle : BarcodeUiState
    data class Error(val message: String) : BarcodeUiState
    data object ImageSaved : BarcodeUiState
}