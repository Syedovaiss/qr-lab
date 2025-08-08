package com.ovais.quickcode.features.create.presentation

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovais.quickcode.analytics.AppAnalyticsManager
import com.ovais.quickcode.features.create.data.CodeFormats
import com.ovais.quickcode.features.create.data.CodeItem
import com.ovais.quickcode.features.create.data.CodeResult
import com.ovais.quickcode.features.create.data.CodeSize
import com.ovais.quickcode.features.create.data.CodeType
import com.ovais.quickcode.features.create.data.CodeValidationParams
import com.ovais.quickcode.features.create.data.CreateCodeParam
import com.ovais.quickcode.features.create.domain.CodeDefaultColorUseCase
import com.ovais.quickcode.features.create.domain.CodeFormatUseCase
import com.ovais.quickcode.features.create.domain.CodeTypeUseCase
import com.ovais.quickcode.features.create.domain.CodeValidationUseCase
import com.ovais.quickcode.features.create.domain.CreateCodeUseCase
import com.ovais.quickcode.features.create.domain.SaveCreatedCodeUseCase
import com.ovais.quickcode.features.history.data.SaveCreatedCodeParam
import com.ovais.quickcode.utils.AnalyticsConstant.CODE_CREATED
import com.ovais.quickcode.utils.AnalyticsConstant.CODE_CREATED_FAILURE
import com.ovais.quickcode.utils.AnalyticsConstant.CODE_CREATED_MESSAGE
import com.ovais.quickcode.utils.AnalyticsConstant.CODE_CREATING
import com.ovais.quickcode.utils.AnalyticsConstant.CODE_CREATION
import com.ovais.quickcode.utils.AnalyticsConstant.CODE_FORMAT
import com.ovais.quickcode.utils.AnalyticsConstant.CODE_TYPE
import com.ovais.quickcode.utils.AnalyticsConstant.DATA
import com.ovais.quickcode.utils.AnalyticsConstant.ERROR
import com.ovais.quickcode.utils.AnalyticsConstant.ERROR_REASON
import com.ovais.quickcode.utils.AnalyticsConstant.HEIGHT_UPDATE
import com.ovais.quickcode.utils.AnalyticsConstant.HEIGHT_UPDATE_MESSAGE
import com.ovais.quickcode.utils.AnalyticsConstant.IS_LOGO_SELECTED
import com.ovais.quickcode.utils.AnalyticsConstant.SELECTED_CODE_FORMAT
import com.ovais.quickcode.utils.AnalyticsConstant.WIDTH_UPDATE
import com.ovais.quickcode.utils.AnalyticsConstant.WIDTH_UPDATE_MESSAGE
import com.ovais.quickcode.utils.DateTimeManager
import com.ovais.quickcode.utils.KeyValue
import com.ovais.quickcode.utils.ValidationResult
import com.ovais.quickcode.utils.file.FileManager
import com.ovais.quickcode.utils.orZero
import com.ovais.quickcode.utils.permissions.PermissionManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

typealias BackgroundColor = Color
typealias ForegroundColor = Color

class CreateCodeViewModel(
    private val codeTypeUseCase: CodeTypeUseCase,
    private val codeFormatUseCase: CodeFormatUseCase,
    private val codeValidationUseCase: CodeValidationUseCase,
    private val createCodeUseCase: CreateCodeUseCase,
    private val permissionManager: PermissionManager,
    private val fileManager: FileManager,
    private val colorCodeUseCase: CodeDefaultColorUseCase,
    private val analyticsManager: AppAnalyticsManager,
    private val saveCreatedCodeUseCase: SaveCreatedCodeUseCase,
    private val dateTimeManager: DateTimeManager
) : ViewModel() {

    private val _defaultColors by lazy {
        MutableStateFlow(
            Pair(
                Color.White,
                Color.Black
            )
        )
    }
    val defaultColors: StateFlow<Pair<Color, Color>>
        get() = _defaultColors.asStateFlow()
    private val _codeSize by lazy { MutableStateFlow(CodeSize(400, 150)) }
    val codeSize: StateFlow<CodeSize>
        get() = _codeSize

    val fileManagerImpl: FileManager
        get() = fileManager
    private val _codeItems by lazy { MutableStateFlow(codeTypeUseCase()) }
    val codeItems: StateFlow<List<CodeItem>>
        get() = _codeItems

    private val _codeFormats by lazy {
        MutableStateFlow(codeFormatUseCase())
    }

    val codeFormats: StateFlow<MutableList<CodeFormats>>
        get() = _codeFormats.asStateFlow()

    private val _errorMessage by lazy { MutableSharedFlow<String>() }
    val errorMessage: SharedFlow<String>
        get() = _errorMessage.asSharedFlow()

    private val _code by lazy { MutableSharedFlow<Bitmap?>() }
    val code: SharedFlow<Bitmap?>
        get() = _code.asSharedFlow()

    private val _permissionArray by lazy { MutableSharedFlow<ArrayList<String>>() }
    val permissionArray: SharedFlow<ArrayList<String>>
        get() = _permissionArray.asSharedFlow()

    init {
        loadDefaultColors()
    }

    private fun loadDefaultColors() {
        viewModelScope.launch {
            val colors = colorCodeUseCase()
            _defaultColors.value = colors
        }
    }

    fun onCodeSelection(format: CodeFormats) {
        analyticsManager.logEvent(
            CODE_FORMAT,
            SELECTED_CODE_FORMAT,
            hashMapOf(DATA to format.title)
        )
        if (format is CodeFormats.QRCode) {
            _codeSize.update {
                codeSize.value.copy(
                    height = 1024,
                    width = 1024
                )
            }
            checkPermissions()
        } else {
            _codeSize.update {
                codeSize.value.copy(
                    height = 150,
                    width = 400
                )
            }
        }
    }

    private fun checkPermissions() {
        val permissions = arrayListOf<String>()
        if (permissionManager.hasCameraPermission.not()) {
            permissions.add(permissionManager.cameraPermission)
        }
        if (permissionManager.hasStoragePermission.not()) {
            permissions.add(permissionManager.storagePermission)
        }
        if (permissionManager.hasNotificationPermission.not()) {
            permissions.add(permissionManager.notificationPermission)
        }
        viewModelScope.launch {
            _permissionArray.emit(permissions)
        }
    }

    fun createCode(
        selectedValues: MutableMap<String, String>,
        selectedType: CodeFormats?,
        type: CodeType,
        colors: Pair<BackgroundColor, ForegroundColor>,
        selectedLogo: Bitmap?
    ) {
        when (val result = validateInputs(selectedValues, type)) {
            is ValidationResult.Valid -> createCodeBasedOnType(
                selectedValues,
                selectedType,
                type,
                colors,
                selectedLogo
            )

            is ValidationResult.InValid -> updateError(result.message)
        }
    }

    private fun updateError(message: String) {
        viewModelScope.launch {
            _errorMessage.emit(message)
        }
    }

    private fun createCodeBasedOnType(
        selectedValues: MutableMap<String, String>,
        selectedType: CodeFormats?,
        type: CodeType,
        colors: Pair<BackgroundColor, ForegroundColor>,
        selectedLogo: Bitmap?
    ) {
        analyticsManager.logEvent(
            CODE_CREATION,
            CODE_CREATING,
            hashMapOf(
                CODE_TYPE to type,
                IS_LOGO_SELECTED to (selectedLogo != null)
            )
        )
        val param = CreateCodeParam(
            selectedValues,
            type,
            selectedType ?: CodeFormats.Code128,
            colors,
            selectedLogo,
            codeSize.value.width,
            codeSize.value.height
        )
        viewModelScope.launch {
            when (val result = createCodeUseCase(param)) {
                is CodeResult.Success -> {
                    analyticsManager.logEvent(
                        CODE_CREATED,
                        CODE_CREATED_MESSAGE
                    )
                    _code.emit(result.code)
                    saveToHistory(
                        selectedValues,
                        selectedType ?: CodeFormats.Code128,
                        type,
                        colors,
                        result.code
                    )
                }

                is CodeResult.Failure -> {
                    analyticsManager.logEvent(
                        CODE_CREATED_FAILURE,
                        ERROR_REASON,
                        hashMapOf(ERROR to result.message)
                    )
                    updateError(result.message)
                }
            }
        }
    }

    fun onWidthUpdate(width: String) {
        analyticsManager.logEvent(
            WIDTH_UPDATE,
            WIDTH_UPDATE_MESSAGE,
            hashMapOf(DATA to width)
        )
        this.codeSize.value.width = width.toIntOrNull().orZero
    }

    fun onHeightUpdate(height: String) {
        analyticsManager.logEvent(
            HEIGHT_UPDATE,
            HEIGHT_UPDATE_MESSAGE,
            hashMapOf(DATA to height)
        )
        this.codeSize.value.height = height.toIntOrNull().orZero
    }

    private fun validateInputs(
        selectedValues: MutableMap<String, String>,
        type: CodeType
    ): ValidationResult {
        return codeValidationUseCase(
            CodeValidationParams(
                selectedValues,
                type
            )
        )
    }

    private fun saveToHistory(
        selectedValues: MutableMap<String, String>,
        selectedType: CodeFormats?,
        type: CodeType,
        colors: Pair<BackgroundColor, ForegroundColor>,
        createdCode: Bitmap?
    ) {
        viewModelScope.launch {
            try {
                val content = selectedValues.map {
                    KeyValue(
                        key = it.key,
                        value = it.value
                    )
                }
                saveCreatedCodeUseCase(
                    param = SaveCreatedCodeParam(
                        content = content,
                        codeType = type,
                        format = selectedType ?: CodeFormats.Code128,
                        foregroundColor = colors.second.toArgb().toString(),
                        backgroundColor = colors.first.toArgb().toString(),
                        width = codeSize.value.width,
                        height = codeSize.value.height,
                        logo = createdCode,
                        createdAt = dateTimeManager.now
                    )
                )
            } catch (e: Exception) {
                analyticsManager.logEvent(
                    "HISTORY_SAVE_ERROR",
                    "Failed to save created code to history",
                    hashMapOf("error" to e.message.toString())
                )
            }
        }
    }

}
