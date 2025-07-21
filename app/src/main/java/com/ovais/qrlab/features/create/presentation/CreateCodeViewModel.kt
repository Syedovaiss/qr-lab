package com.ovais.qrlab.features.create.presentation

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovais.qrlab.features.create.data.CodeFormats
import com.ovais.qrlab.features.create.data.CodeItem
import com.ovais.qrlab.features.create.data.CodeResult
import com.ovais.qrlab.features.create.data.CodeType
import com.ovais.qrlab.features.create.data.CodeValidationParams
import com.ovais.qrlab.features.create.data.CreateCodeParam
import com.ovais.qrlab.features.create.domain.CodeFormatUseCase
import com.ovais.qrlab.features.create.domain.CodeTypeUseCase
import com.ovais.qrlab.features.create.domain.CodeValidationUseCase
import com.ovais.qrlab.features.create.domain.CreateCodeUseCase
import com.ovais.qrlab.logger.QRLogger
import com.ovais.qrlab.utils.ValidationResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreateCodeViewModel(
    private val codeTypeUseCase: CodeTypeUseCase,
    private val logger: QRLogger,
    private val codeFormatUseCase: CodeFormatUseCase,
    private val codeValidationUseCase: CodeValidationUseCase,
    private val createCodeUseCase: CreateCodeUseCase
) : ViewModel() {
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

    fun createCode(
        selectedValues: MutableMap<String, String>,
        selectedType: CodeFormats?,
        type: CodeType
    ) {
        when (val result = validateInputs(selectedValues, type)) {
            is ValidationResult.Valid -> createCodeBasedOnType(selectedValues, selectedType, type)
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
        type: CodeType
    ) {
        val param = CreateCodeParam(
            selectedValues,
            type,
            selectedType
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
