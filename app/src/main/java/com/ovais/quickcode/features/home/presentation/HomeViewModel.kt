package com.ovais.quickcode.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovais.quickcode.features.home.data.CardItemType
import com.ovais.quickcode.features.home.domain.CardItemsUseCase
import com.ovais.quickcode.features.home.domain.GetTermsAndConditionsUseCase
import com.ovais.quickcode.features.home.domain.GetUserInfoUseCase
import com.ovais.quickcode.logger.AppLogger
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val cardItemsUseCase: CardItemsUseCase,
    private val logger: AppLogger,
    private val userInfoUseCase: GetUserInfoUseCase,
    private val termsAndConditionsUrl: GetTermsAndConditionsUseCase
) : ViewModel() {

    private val _nextDestination by lazy { MutableSharedFlow<HomeAction>() }
    val nextDestination: SharedFlow<HomeAction>
        get() = _nextDestination.asSharedFlow()

    private val _uiState by lazy { MutableStateFlow<HomeUiState>(HomeUiState.Loading) }
    val uiState: StateFlow<HomeUiState>
        get() = _uiState.asStateFlow()

    private val _termsAndConditionsIUrl by lazy { MutableSharedFlow<String>() }
    val termsAndConditionsIUrl: SharedFlow<String>
        get() = _termsAndConditionsIUrl

    init {
        initUiData()
    }

    private fun initUiData() {
        viewModelScope.launch {
            _uiState.update {
                HomeUiState.Loaded(
                    HomeUiData(
                        userInfo = userInfoUseCase(),
                        cardItem = cardItemsUseCase()
                    )
                )
            }
        }
    }

    fun onAction(type: CardItemType) {
        val intent = when (type) {
            is CardItemType.Create -> {
                logger.logInfo("Navigating for qr creation")
                HomeAction.OnCreateCode
            }

            is CardItemType.Scan -> {
                logger.logInfo("Navigating for qr scanning")
                HomeAction.OnScanCode
            }

            is CardItemType.History -> {
                logger.logInfo("Navigating for history")
                HomeAction.OnHistoryClicked
            }

            is CardItemType.Settings -> {
                logger.logInfo("Navigating for settings")
                HomeAction.OnSettingsClicked

            }
        }
        updateDestination(intent)
    }

    private fun updateDestination(intent: HomeAction) {
        viewModelScope.launch {
            _nextDestination.emit(intent)
        }
    }

    fun onTermsAndConditions() {
        viewModelScope.launch {
            _termsAndConditionsIUrl.emit(termsAndConditionsUrl())
        }
    }
}