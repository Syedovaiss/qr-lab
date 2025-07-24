package com.ovais.qrlab.features.scan_qr.domain

import com.ovais.qrlab.features.scan_qr.data.ScanCodeParam
import com.ovais.qrlab.features.scan_qr.data.ScanRepository
import com.ovais.qrlab.features.scan_qr.data.ScanResult
import com.ovais.qrlab.utils.usecase.SuspendParameterizedUseCase
import kotlinx.coroutines.delay

interface ScanCodeUseCase : SuspendParameterizedUseCase<ScanCodeParam, ScanResult>
class DefaultScanCodeUseCase(
    private val repository: ScanRepository
) : ScanCodeUseCase {
    override suspend fun invoke(param: ScanCodeParam): ScanResult {
        delay(500)
        return repository.scan(param)
    }
}