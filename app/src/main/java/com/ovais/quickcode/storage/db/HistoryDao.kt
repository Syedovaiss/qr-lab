package com.ovais.quickcode.storage.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ovais.quickcode.features.history.data.CreatedCodeEntity
import com.ovais.quickcode.features.history.data.ScannedCodeEntity

@Dao
interface HistoryDao {

    // Created Codes Operations
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertCreatedCode(createdCode: CreatedCodeEntity): Long

    @Query("SELECT * FROM code_created ORDER BY created_at DESC")
    suspend fun getAllCreatedCodes(): List<CreatedCodeEntity>

    @Query("SELECT * FROM code_created ORDER BY created_at DESC")
    fun getAllCreatedCodeByDescOrder(): List<CreatedCodeEntity>

    @Query("DELETE FROM code_created WHERE code_id = :id")
    suspend fun deleteCreatedCodeById(id: Long)

    // Scanned Codes Operations
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertScannedCode(scannedCode: ScannedCodeEntity): Long

    @Query("SELECT * FROM code_scanned ORDER BY scannedAt DESC")
    fun getScannedCodesDescending(): List<ScannedCodeEntity>

    @Query("DELETE FROM code_scanned WHERE id = :id")
    suspend fun deleteScannedCodeById(id: Long)
}