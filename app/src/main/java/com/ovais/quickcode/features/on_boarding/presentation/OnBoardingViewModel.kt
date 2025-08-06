package com.ovais.quickcode.features.on_boarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovais.quickcode.features.on_boarding.data.OnboardingPage
import com.ovais.quickcode.features.on_boarding.domain.GetOnBoardingItemsUseCase
import com.ovais.quickcode.features.on_boarding.domain.OnBoardingStatus
import com.ovais.quickcode.features.on_boarding.domain.UpdateOnBoardingStatusUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OnBoardingViewModel(
    private val getOnBoardingItemsUseCase: GetOnBoardingItemsUseCase,
    private val updateOnBoardingStatusUseCase: UpdateOnBoardingStatusUseCase
) : ViewModel() {
    private val _onBoardingItems by lazy { MutableStateFlow(getOnBoardingItemsUseCase()) }
    val onBoardingItems: StateFlow<List<OnboardingPage>>
        get() = _onBoardingItems

    fun onOnBoardingFinish(onFinish: () -> Unit) {
        viewModelScope.launch {
            when (updateOnBoardingStatusUseCase()) {
                is OnBoardingStatus.Finished -> onFinish()
            }
        }

    }
}