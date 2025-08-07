package com.ovais.quickcode.features.history.data

interface HistoryRepository {
    fun getCreatedCodes(): List<HistoryItem>
    fun getScannedCodes(): List<HistoryItem>
    suspend fun saveCreatedCode(
        param: SaveCreatedCodeParam
    ): SaveHistoryResult

    suspend fun saveScannedCode(
        param: SaveScannedCodeParam
    ): SaveHistoryResult

    suspend fun deleteCreatedCode(id: Long)
    suspend fun deleteScannedCode(id: Long)
    suspend fun getCreatedCodesCount(): Int
    suspend fun getScannedCodesCount(): Int
}
