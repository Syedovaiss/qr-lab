package com.ovais.quickcode.features.scan_qr.data

fun interface ScanRepository {
    suspend fun scan(param: ScanCodeParam): ScanResult
}