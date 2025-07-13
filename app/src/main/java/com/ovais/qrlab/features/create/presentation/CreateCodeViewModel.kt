package com.ovais.qrlab.features.create.presentation

import androidx.lifecycle.ViewModel
import com.ovais.qrlab.features.create.data.CodeFormats
import com.ovais.qrlab.features.create.data.CodeItem
import com.ovais.qrlab.features.create.data.CodeType
import com.ovais.qrlab.features.create.domain.CodeFormatUseCase
import com.ovais.qrlab.features.create.domain.CodeTypeUseCase
import com.ovais.qrlab.logger.QRLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateCodeViewModel(
    private val codeTypeUseCase: CodeTypeUseCase,
    private val logger: QRLogger,
    private val codeFormatUseCase: CodeFormatUseCase
) : ViewModel() {
    private val _codeItems by lazy { MutableStateFlow(codeTypeUseCase()) }
    val codeItems: StateFlow<List<CodeItem>>
        get() = _codeItems

    private val _codeFormats by lazy {
        MutableStateFlow(codeFormatUseCase())
    }

    val codeFormats: StateFlow<MutableList<CodeFormats>>
        get() = _codeFormats.asStateFlow()


    fun createCode(
        selectedValues: MutableMap<String, String>,
        selectedType: CodeFormats?,
        type: CodeType
    ) {
        logger.logInfo("test...")
    }
}
