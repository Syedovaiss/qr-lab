package com.ovais.quickcode.storage.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ovais.quickcode.features.history.data.CreatedCodeEntity
import com.ovais.quickcode.features.history.data.ScannedCodeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    // Created Codes Operations
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertCreatedCode(createdCode: CreatedCodeEntity): Long

    @Query("SELECT * FROM code_created ORDER BY createdAt DESC")
    fun getAllCreatedCodes(): Flow<List<CreatedCodeEntity>>

    @Query("SELECT * FROM code_created WHERE codeType = :codeType ORDER BY createdAt DESC")
    fun getCreatedCodesByType(codeType: String): Flow<List<CreatedCodeEntity>>

    @Query("SELECT * FROM code_created WHERE content LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun searchCreatedCodes(query: String): Flow<List<CreatedCodeEntity>>

    @Query("SELECT * FROM code_created ORDER BY createdAt ASC")
    fun getCreatedCodesAscending(): Flow<List<CreatedCodeEntity>>

    @Query("SELECT * FROM code_created ORDER BY createdAt DESC")
    fun getCreatedCodesDescending(): List<CreatedCodeEntity>

    @Delete
    suspend fun deleteCreatedCode(createdCode: CreatedCodeEntity)

    @Query("DELETE FROM code_created WHERE id = :id")
    suspend fun deleteCreatedCodeById(id: Long)

    @Query("DELETE FROM code_created")
    suspend fun deleteAllCreatedCodes()

    // Scanned Codes Operations
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertScannedCode(scannedCode: ScannedCodeEntity): Long

    @Query("SELECT * FROM code_scanned ORDER BY scannedAt DESC")
    fun getAllScannedCodes(): Flow<List<ScannedCodeEntity>>

    @Query("SELECT * FROM code_scanned WHERE content LIKE '%' || :query || '%' ORDER BY scannedAt DESC")
    fun searchScannedCodes(query: String): Flow<List<ScannedCodeEntity>>

    @Query("SELECT * FROM code_scanned ORDER BY scannedAt ASC")
    fun getScannedCodesAscending(): Flow<List<ScannedCodeEntity>>

    @Query("SELECT * FROM code_scanned ORDER BY scannedAt DESC")
    fun getScannedCodesDescending(): List<ScannedCodeEntity>

    @Delete
    suspend fun deleteScannedCode(scannedCode: ScannedCodeEntity)

    @Query("DELETE FROM code_scanned WHERE id = :id")
    suspend fun deleteScannedCodeById(id: Long)

    @Query("DELETE FROM code_scanned")
    suspend fun deleteAllScannedCodes()

    // Statistics
    @Query("SELECT COUNT(*) FROM code_created")
    suspend fun getCreatedCodesCount(): Int

    @Query("SELECT COUNT(*) FROM code_scanned")
    suspend fun getScannedCodesCount(): Int

    @Query("SELECT COUNT(*) FROM code_created WHERE codeType = :codeType")
    suspend fun getCreatedCodesCountByType(codeType: String): Int
}