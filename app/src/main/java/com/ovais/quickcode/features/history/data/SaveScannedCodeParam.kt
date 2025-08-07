package com.ovais.quickcode.features.history.data

import android.graphics.Bitmap
import com.ovais.quickcode.utils.KeyValue

data class SaveScannedCodeParam(
    val content: List<KeyValue>,
    val bitmap: Bitmap?,
    val scannedAt: String
)
