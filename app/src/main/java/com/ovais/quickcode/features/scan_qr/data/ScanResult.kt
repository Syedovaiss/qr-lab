package com.ovais.quickcode.features.scan_qr.data

sealed interface ScanResult {
    data class Success(val content: String) : ScanResult
    data class Failure(val message: String) : ScanResult
}