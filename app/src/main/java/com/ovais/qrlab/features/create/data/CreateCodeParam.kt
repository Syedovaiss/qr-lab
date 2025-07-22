package com.ovais.qrlab.features.create.data

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color

data class CreateCodeParam(
    val selectedContentMap: MutableMap<String, String>,
    val type: CodeType,
    val format: CodeFormats?,
    val colors: Pair<Color, Color>,
    val logo: Bitmap?,
    val width: Int,
    val height: Int
)
