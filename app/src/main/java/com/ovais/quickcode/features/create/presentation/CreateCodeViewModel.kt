package com.ovais.quickcode.features.create.presentation

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val colorCodeUseCase: CodeDefaultColorUseCase
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
    private val _codeSize by lazy { MutableStateFlow(CodeSize(600, 600)) }
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
        if (format is CodeFormats.QRCode) {
            checkPermissions()
        } else {
            _codeSize.update {
                codeSize.value.copy(
                    height = 150
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
        val param = CreateCodeParam(
            selectedValues,
            type,
            selectedType,
            colors,
            selectedLogo,
            codeSize.value.width,
            codeSize.value.height
        )
        viewModelScope.launch {
            when (val result = createCodeUseCase(param)) {
                is CodeResult.Success -> {
                    _code.emit(result.code)
                }

                is CodeResult.Failure -> {
                    updateError(result.message)
                }
            }
        }
    }

    fun onWidthUpdate(width: String) {
        this.codeSize.value.width = width.toIntOrNull().orZero
    }

    fun onHeightUpdate(height: String) {
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

}
