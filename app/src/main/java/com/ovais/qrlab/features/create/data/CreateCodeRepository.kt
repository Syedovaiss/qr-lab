package com.ovais.qrlab.features.create.data

interface CreateCodeRepository {
    suspend fun createBarcode(
        selectedContentMap: MutableMap<String, String>,
        format: CodeFormats?,
        type: CodeType
    ): CodeResult

    suspend fun createQRBarcode(
        selectedContentMap: MutableMap<String, String>,
        type: CodeType
    ): CodeResult
}