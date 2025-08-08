package com.ovais.quickcode.features.history.data

import android.graphics.Bitmap

data class SaveScannedCodeParam(
    val content: String,
    val bitmap: Bitmap?,
    val scannedAt: String
)
