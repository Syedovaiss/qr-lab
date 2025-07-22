package com.ovais.qrlab.features.scan_qr.domain

import androidx.camera.core.ImageProxy
import com.ovais.qrlab.features.scan_qr.data.ScanRepository
import com.ovais.qrlab.features.scan_qr.data.ScanResult
import com.ovais.qrlab.utils.usecase.SuspendParameterizedUseCase

interface ScanCodeUseCase : SuspendParameterizedUseCase<ImageProxy, ScanResult>
class DefaultScanCodeUseCase(
    private val repository: ScanRepository
) : ScanCodeUseCase {
    override suspend fun invoke(param: ImageProxy): ScanResult {
        return repository.scan(param)
    }
}