package com.ovais.quickcode.features.splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovais.quickcode.core.app.domain.StartDestinationUseCase
import com.ovais.quickcode.navigation.Routes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val startDestinationUseCase: StartDestinationUseCase
) : ViewModel() {

    private val _startDestination by lazy { MutableStateFlow<Routes?>(null) }
    val startDestination: StateFlow<Routes?>
        get() = _startDestination.asStateFlow()

    init {
        initStartDestination()
    }

    private fun initStartDestination() {
        viewModelScope.launch {
            _startDestination.value = startDestinationUseCase()
        }
    }
}