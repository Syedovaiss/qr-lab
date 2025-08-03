package com.ovais.quickcode.features.settings.presentation

import com.ovais.quickcode.features.settings.data.AppSettings


data class SettingsUiState(
    val settings: AppSettings = AppSettings(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showColorPicker: Boolean = false,
    val showBackgroundColorPicker: Boolean = false,
    val showExportFormatDialog: Boolean = false,
    val showClearHistoryDialog: Boolean = false,
    val showExportHistoryDialog: Boolean = false,
    val showLanguageDialog: Boolean = false,
    val showPrivacyPolicyDialog: Boolean = false,
    val selectedColorType: ColorType = ColorType.FOREGROUND
)