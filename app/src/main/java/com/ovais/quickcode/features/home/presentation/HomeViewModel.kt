package com.ovais.quickcode.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovais.quickcode.features.home.data.CardItemType
import com.ovais.quickcode.features.home.data.HomeCardItem
import com.ovais.quickcode.features.home.domain.CardItemsUseCase
import com.ovais.quickcode.logger.AppLogger
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val cardItemsUseCase: CardItemsUseCase,
    private val logger: AppLogger
) : ViewModel() {

    private val _nextDestination by lazy { MutableSharedFlow<HomeIntent>() }
    val nextDestination: SharedFlow<HomeIntent>
        get() = _nextDestination.asSharedFlow()

    private val _cardItems by lazy { MutableStateFlow(cardItemsUseCase()) }
    val cardItems: StateFlow<List<HomeCardItem>>
        get() = _cardItems.asStateFlow()

    fun onAction(type: CardItemType) {
        val intent = when (type) {
            is CardItemType.Create -> {
                logger.logInfo("Navigating for qr creation")
                HomeIntent.OnCreateCode
            }

            is CardItemType.Scan -> {
                logger.logInfo("Navigating for qr scanning")
                HomeIntent.OnScanCode
            }

            is CardItemType.History -> {
                logger.logInfo("Navigating for history")
                HomeIntent.OnHistoryClicked
            }
            is CardItemType.Settings -> {
                logger.logInfo("Navigating for settings")
                HomeIntent.OnSettingsClicked

            }
        }
        updateDestination(intent)
    }

    private fun updateDestination(intent: HomeIntent) {
        viewModelScope.launch {
            _nextDestination.emit(intent)
        }
    }
}