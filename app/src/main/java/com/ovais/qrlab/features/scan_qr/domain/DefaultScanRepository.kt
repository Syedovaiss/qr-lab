package com.ovais.qrlab.features.scan_qr.domain

import androidx.camera.core.ImageProxy
import com.ovais.qrlab.barcode_manger.BarcodeManager
import com.ovais.qrlab.features.scan_qr.data.ScanRepository
import com.ovais.qrlab.features.scan_qr.data.ScanResult

class DefaultScanRepository(
    private val barcodeManager: BarcodeManager
) : ScanRepository {
    override suspend fun scan(imageProxy: ImageProxy): ScanResult {
        return barcodeManager.scanCode(imageProxy)
    }
}