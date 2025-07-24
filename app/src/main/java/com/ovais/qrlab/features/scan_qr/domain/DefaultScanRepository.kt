package com.ovais.qrlab.features.scan_qr.domain

import com.ovais.qrlab.barcode_manger.BarcodeManager
import com.ovais.qrlab.features.scan_qr.data.ScanCodeParam
import com.ovais.qrlab.features.scan_qr.data.ScanCodeParamType
import com.ovais.qrlab.features.scan_qr.data.ScanRepository
import com.ovais.qrlab.features.scan_qr.data.ScanResult

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