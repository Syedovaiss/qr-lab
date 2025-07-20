package com.ovais.qrlab.features.create.data

data class CreateCodeParam(
    val selectedContentMap: MutableMap<String, String>,
    val type: CodeType,
    val format: CodeFormats?
)
