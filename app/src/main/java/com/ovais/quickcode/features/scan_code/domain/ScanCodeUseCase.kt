package com.ovais.quickcode.features.scan_code.domain

import com.ovais.quickcode.features.scan_code.data.ScanCodeParam
import com.ovais.quickcode.features.scan_code.data.ScanRepository
import com.ovais.quickcode.features.scan_code.data.ScanResult
import com.ovais.quickcode.utils.usecase.SuspendParameterizedUseCase
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