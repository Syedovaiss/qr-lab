package com.ovais.qrlab.features.scan_qr.data

import androidx.camera.core.ImageProxy

fun interface ScanRepository {
    suspend fun scan(imageProxy: ImageProxy): ScanResult
}