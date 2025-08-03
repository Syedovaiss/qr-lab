package com.ovais.quickcode.features.scan_code.data

fun interface ScanRepository {
    suspend fun scan(param: ScanCodeParam): ScanResult
}