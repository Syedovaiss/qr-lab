package com.ovais.quickcode.features.create.data

data class CodeValidationParams(
    val selectedContentMap: MutableMap<String, String>,
    val type: CodeType,
)
