package com.ovais.quickcode.features.create.domain

import com.ovais.quickcode.features.create.data.CodeFormats
import com.ovais.quickcode.features.create.data.CodeResult
import com.ovais.quickcode.features.create.data.CreateCodeParam
import com.ovais.quickcode.features.create.data.CreateCodeRepository
import com.ovais.quickcode.utils.usecase.SuspendParameterizedUseCase

interface CreateCodeUseCase : SuspendParameterizedUseCase<CreateCodeParam, CodeResult>

class DefaultCreateCodeUseCase(
    private val repository: CreateCodeRepository
) : CreateCodeUseCase {
    override suspend fun invoke(param: CreateCodeParam): CodeResult {
        return if (param.format !is CodeFormats.QRCode) {
            createBarcode(param)
        } else {
            createQR(param)
        }
    }

    private suspend fun createBarcode(param: CreateCodeParam): CodeResult {
        return repository.createBarcode(
            param.selectedContentMap,
            param.format,
            param.type,
            param.width,
            param.height
        )
    }

    private suspend fun createQR(param: CreateCodeParam): CodeResult {
        return repository.createQRCode(
            param.selectedContentMap,
            param.type,
            param.colors,
            param.logo,
            param.width,
            param.height
        )
    }
}