package com.ovais.quickcode.features.scan_code.data

import android.graphics.Bitmap

sealed interface ScanResult {
    data class Success(
        val content: String,
        val bitmap: Bitmap?
    ) : ScanResult

    data class Failure(val message: String) : ScanResult
}