package com.ovais.quickcode.features.create.data

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color

interface CreateCodeRepository {
    suspend fun createBarcode(
        selectedContentMap: MutableMap<String, String>,
        format: CodeFormats?,
        type: CodeType,
        width: Int,
        height: Int
    ): CodeResult

    suspend fun createQRCode(
        selectedContentMap: MutableMap<String, String>,
        type: CodeType,
        colors: Pair<Color, Color>,
        logo: Bitmap?,
        width: Int,
        height: Int
    ): CodeResult
}