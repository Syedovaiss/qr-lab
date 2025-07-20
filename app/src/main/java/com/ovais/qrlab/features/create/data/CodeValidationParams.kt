package com.ovais.qrlab.features.create.data

data class CodeValidationParams(
    val selectedContentMap: MutableMap<String, String>,
    val type: CodeType,
)
