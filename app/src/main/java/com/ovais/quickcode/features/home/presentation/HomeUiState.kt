package com.ovais.quickcode.features.home.presentation

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Loaded(val uiData: HomeUiData) : HomeUiState
}