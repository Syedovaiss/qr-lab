package com.ovais.quickcode.features.splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovais.quickcode.core.app.domain.StartDestinationUseCase
import com.ovais.quickcode.navigation.Routes
import com.ovais.quickcode.storage.QuickCodeConfigurationManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val startDestinationUseCase: StartDestinationUseCase,
    private val configurationManager: QuickCodeConfigurationManager
) : ViewModel() {

    private val _startDestination = MutableStateFlow<Routes?>(null)
    val startDestination: StateFlow<Routes?> get() = _startDestination

    init {
        viewModelScope.launch {
            _startDestination.value = startDestinationUseCase()
            launch { configurationManager.activate() }
        }
    }
}