package com.ovais.qrlab.features.create.domain

import com.ovais.qrlab.features.create.data.CodeFormats
import com.ovais.qrlab.features.create.data.CodeResult
import com.ovais.qrlab.features.create.data.CreateCodeParam
import com.ovais.qrlab.features.create.data.CreateCodeRepository
import com.ovais.qrlab.utils.usecase.SuspendParameterizedUseCase

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
        return repository.createBarcode(param.selectedContentMap,param.format,param.type)
    }

    private suspend fun createQR(param: CreateCodeParam): CodeResult {
        return repository.createQRBarcode(param.selectedContentMap,param.type)
    }
}