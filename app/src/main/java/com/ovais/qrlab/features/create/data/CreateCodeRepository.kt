package com.ovais.qrlab.features.create.data

import androidx.compose.ui.graphics.Color

interface CreateCodeRepository {
    suspend fun createBarcode(
        selectedContentMap: MutableMap<String, String>,
        format: CodeFormats?,
        type: CodeType
    ): CodeResult

    suspend fun createQRCode(
        selectedContentMap: MutableMap<String, String>,
        type: CodeType,
        colors: Pair<Color, Color>
    ): CodeResult
}