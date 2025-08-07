package com.ovais.quickcode.features.history.data

import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun getCreatedCodes(): Flow<List<HistoryItem>>
    fun getScannedCodes(): Flow<List<HistoryItem>>
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
