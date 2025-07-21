package com.ovais.qrlab.features.create.data

import androidx.compose.ui.graphics.Color

data class CreateCodeParam(
    val selectedContentMap: MutableMap<String, String>,
    val type: CodeType,
    val format: CodeFormats?,
    val colors: Pair<Color, Color>
)
