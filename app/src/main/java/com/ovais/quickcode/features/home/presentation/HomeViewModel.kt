package com.ovais.quickcode.features.home.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovais.quickcode.auth.AuthManager
import com.ovais.quickcode.auth.AuthResult
import com.ovais.quickcode.auth.AuthType
import com.ovais.quickcode.features.home.data.AuthState
import com.ovais.quickcode.features.home.data.CardItemType
import com.ovais.quickcode.features.home.data.LoggedInParam
import com.ovais.quickcode.features.home.domain.CanLoginUseCase
import com.ovais.quickcode.features.home.domain.CardItemsUseCase
import com.ovais.quickcode.features.home.domain.LoginResultUseCase
import com.ovais.quickcode.logger.AppLogger
import com.ovais.quickcode.utils.usecase.GetTermsAndConditionsUseCase
import com.ovais.quickcode.utils.usecase.GetUserInfoUseCase
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
    private val termsAndConditionsUrl: GetTermsAndConditionsUseCase,
    private val authManager: AuthManager,
    private val loginResultUseCase: LoginResultUseCase,
    private val canLoginUseCase: CanLoginUseCase
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

    private val _errorMessage by lazy { MutableSharedFlow<String>() }
    val errorMessage: SharedFlow<String>
        get() = _errorMessage.asSharedFlow()

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

    fun onUserClick(context: Context) {
        viewModelScope.launch {
            if (canLoginUseCase()) {
                authManager.signIn(context, AuthType.Google) { result ->
                    when (result) {
                        is AuthResult.Success -> {
                            updateLoginResult(
                                LoggedInParam(
                                    email = result.email,
                                    name = result.name,
                                    avatar = result.avatar,
                                    type = AuthType.Google

                                )
                            )
                        }

                        is AuthResult.Failure -> {
                            updateError(result.message)
                        }
                    }
                }
            }
        }
    }

    private fun updateLoginResult(param: LoggedInParam) {
        viewModelScope.launch {
            when (val result = loginResultUseCase(param)) {
                is AuthState.LoggedIn -> {
                    logger.logInfo("Login result saved!:$result")
                    updateUI()
                }

                is AuthState.Failure -> {
                    logger.logException("Couldn't save results")
                }
            }
        }
    }

    private fun updateUI() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loaded(
                (uiState.value as HomeUiState.Loaded).uiData.copy(
                    userInfo = userInfoUseCase()
                )
            )
        }
    }

    private fun updateError(message: String) {
        viewModelScope.launch { _errorMessage.emit(message) }
    }
}