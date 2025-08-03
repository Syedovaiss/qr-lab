package com.ovais.quickcode.features.scan_code.domain

import com.ovais.quickcode.barcode_manger.BarcodeManager
import com.ovais.quickcode.features.scan_code.data.ScanCodeParam
import com.ovais.quickcode.features.scan_code.data.ScanCodeParamType
import com.ovais.quickcode.features.scan_code.data.ScanRepository
import com.ovais.quickcode.features.scan_code.data.ScanResult

class DefaultScanRepository(
    private val barcodeManager: BarcodeManager
) : ScanRepository {
    override suspend fun scan(param: ScanCodeParam): ScanResult {
        return when (param.type) {
            is ScanCodeParamType.ImageProxy -> {
                param.imageProxy?.let {
                    barcodeManager.scanCode(param.imageProxy)
                } ?: run {
                    ScanResult.Failure("Image Proxy Can't be null")
                }
            }

            is ScanCodeParamType.Uri -> {
                param.uri?.let {
                    barcodeManager.scanCode(param.uri)
                } ?: run {
                    ScanResult.Failure("Uri Can't be null")
                }
            }

            null -> ScanResult.Failure("Please provide param type!")
        }
    }
}