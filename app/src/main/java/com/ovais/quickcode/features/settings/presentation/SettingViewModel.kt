package com.ovais.quickcode.features.settings.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovais.quickcode.features.settings.domain.GetPrivacyPolicyUseCase
import com.ovais.quickcode.features.settings.domain.UpdateSettingUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingViewModel(
    private val updateSettingUseCase: UpdateSettingUseCase,
    private val privacyPolicyUseCase: GetPrivacyPolicyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _privacyPolicyUrl by lazy { MutableSharedFlow<String>() }
    val privacyPolicyUrl: SharedFlow<String>
        get() = _privacyPolicyUrl

    init {
        initializeDefaultSettings()
    }

    private fun fetchSettings() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                updateSettingUseCase.getSettings().collect { settings ->
                    _uiState.value = _uiState.value.copy(
                        settings = settings,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

    private fun initializeDefaultSettings() {
        viewModelScope.launch {
            updateSettingUseCase.initializeDefaultSettings()
        }
    }

    fun updateForegroundColor(color: Color) {
        viewModelScope.launch {
            try {
                updateSettingUseCase.updateForegroundColor(color)
                _uiState.value = _uiState.value.copy(
                    settings = _uiState.value.settings.copy(foregroundColor = color)
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun updateBackgroundColor(color: Color) {
        viewModelScope.launch {
            try {
                updateSettingUseCase.updateBackgroundColor(color)
                _uiState.value = _uiState.value.copy(
                    settings = _uiState.value.settings.copy(backgroundColor = color)
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun updateQRFormat(format: String) {
        viewModelScope.launch {
            try {
                updateSettingUseCase.updateQRFormat(format)
                _uiState.value = _uiState.value.copy(
                    settings = _uiState.value.settings.copy(qrFormat = format),
                    showExportFormatDialog = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun updateVibrationSetting(enabled: Boolean) {
        viewModelScope.launch {
            try {
                updateSettingUseCase.updateVibrationSetting(enabled)
                _uiState.value = _uiState.value.copy(
                    settings = _uiState.value.settings.copy(canVibrateOnScan = enabled)
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun updateBeepSetting(enabled: Boolean) {
        viewModelScope.launch {
            try {
                updateSettingUseCase.updateBeepSetting(enabled)
                _uiState.value = _uiState.value.copy(
                    settings = _uiState.value.settings.copy(canBeepOnScan = enabled)
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun updateAutoCopySetting(enabled: Boolean) {
        viewModelScope.launch {
            try {
                updateSettingUseCase.updateAutoCopySetting(enabled)
                _uiState.value = _uiState.value.copy(
                    settings = _uiState.value.settings.copy(canAutoCopyOnScan = enabled)
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun updateAutoOpenURLSetting(enabled: Boolean) {
        viewModelScope.launch {
            try {
                updateSettingUseCase.updateAutoOpenURLSetting(enabled)
                _uiState.value = _uiState.value.copy(
                    settings = _uiState.value.settings.copy(canAutoOpenURLOnScan = enabled)
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun updateLocale(locale: String) {
        viewModelScope.launch {
            try {
                updateSettingUseCase.updateLocale(locale)
                _uiState.value = _uiState.value.copy(
                    settings = _uiState.value.settings.copy(locale = locale),
                    showLanguageDialog = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun updateAnonymousUsageDataSetting(enabled: Boolean) {
        viewModelScope.launch {
            try {
                updateSettingUseCase.updateAnonymousUsageDataSetting(enabled)
                _uiState.value = _uiState.value.copy(
                    settings = _uiState.value.settings.copy(canSendAnonymousUsageData = enabled)
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            try {
                updateSettingUseCase.clearHistory()
                _uiState.value = _uiState.value.copy(showClearHistoryDialog = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun exportHistory(format: String) {
        viewModelScope.launch {
            try {
                updateSettingUseCase.exportHistory(format)
                _uiState.value = _uiState.value.copy(showExportHistoryDialog = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    // UI State Management
    fun showColorPicker(colorType: ColorType) {
        _uiState.value = _uiState.value.copy(
            showColorPicker = true,
            selectedColorType = colorType
        )
    }

    fun hideColorPicker() {
        _uiState.value = _uiState.value.copy(showColorPicker = false)
    }

    fun showBackgroundColorPicker() {
        _uiState.value = _uiState.value.copy(showBackgroundColorPicker = true)
    }

    fun hideBackgroundColorPicker() {
        _uiState.value = _uiState.value.copy(showBackgroundColorPicker = false)
    }

    fun showExportFormatDialog() {
        _uiState.value = _uiState.value.copy(showExportFormatDialog = true)
    }

    fun hideExportFormatDialog() {
        _uiState.value = _uiState.value.copy(showExportFormatDialog = false)
    }

    fun showClearHistoryDialog() {
        _uiState.value = _uiState.value.copy(showClearHistoryDialog = true)
    }

    fun hideClearHistoryDialog() {
        _uiState.value = _uiState.value.copy(showClearHistoryDialog = false)
    }

    fun showExportHistoryDialog() {
        _uiState.value = _uiState.value.copy(showExportHistoryDialog = true)
    }

    fun hideExportHistoryDialog() {
        _uiState.value = _uiState.value.copy(showExportHistoryDialog = false)
    }

    fun showLanguageDialog() {
        _uiState.value = _uiState.value.copy(showLanguageDialog = true)
    }

    fun hideLanguageDialog() {
        _uiState.value = _uiState.value.copy(showLanguageDialog = false)
    }

    fun showPrivacyPolicyDialog() {
        _uiState.value = _uiState.value.copy(showPrivacyPolicyDialog = true)
    }

    fun hidePrivacyPolicyDialog() {
        _uiState.value = _uiState.value.copy(showPrivacyPolicyDialog = false)
    }

    fun showAboutDialog() {
        _uiState.value = _uiState.value.copy(showAboutDialog = true)
    }

    fun hideAboutDialog() {
        _uiState.value = _uiState.value.copy(showAboutDialog = false)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun openPrivacyPolicy() {
        viewModelScope.launch {
            _privacyPolicyUrl.emit(privacyPolicyUseCase())
        }
    }

    fun initSettings() {

        fetchSettings()
    }
}