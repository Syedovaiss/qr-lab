package com.ovais.qrlab.features.scan_qr.domain

import com.ovais.qrlab.features.scan_qr.data.ScanCodeParam
import com.ovais.qrlab.features.scan_qr.data.ScanRepository
import com.ovais.qrlab.features.scan_qr.data.ScanResult
import com.ovais.qrlab.utils.usecase.SuspendParameterizedUseCase

interface ScanCodeUseCase : SuspendParameterizedUseCase<ScanCodeParam, ScanResult>
class DefaultScanCodeUseCase(
    private val repository: ScanRepository
) : ScanCodeUseCase {
    override suspend fun invoke(param: ScanCodeParam): ScanResult {
        return repository.scan(param)
    }
}