package com.ovais.quickcode.features.history.domain

import com.ovais.quickcode.features.history.data.CreatedCodeEntity
import com.ovais.quickcode.features.history.data.HistoryItem
import com.ovais.quickcode.features.history.data.HistoryRepository
import com.ovais.quickcode.features.history.data.SaveCreatedCodeParam
import com.ovais.quickcode.features.history.data.SaveHistoryResult
import com.ovais.quickcode.features.history.data.SaveScannedCodeParam
import com.ovais.quickcode.features.history.data.ScannedCodeEntity
import com.ovais.quickcode.storage.db.HistoryDao
import com.ovais.quickcode.utils.toKeyValue
import timber.log.Timber


class DefaultHistoryRepository(
    private val historyDao: HistoryDao
) : HistoryRepository {

    override fun getCreatedCodes(): List<HistoryItem> {
        return historyDao.getAllCreatedCodeByDescOrder().map { entity ->
            HistoryItem(
                id = entity.id,
                content = entity.content,
                codeType = entity.codeType,
                format = entity.format,
                timestamp = entity.createdAt,
                foregroundColor = entity.foregroundColor,
                backgroundColor = entity.backgroundColor,
                width = entity.width,
                height = entity.height,
                logo = entity.image
            )
        }
    }

    override fun getScannedCodes(): List<HistoryItem> {
        return historyDao.getScannedCodesDescending().map { entity ->
            HistoryItem(
                id = entity.id,
                content = listOf(entity.content.toKeyValue),
                timestamp = entity.scannedAt,
                logo = entity.bitmap
            )
        }
    }

    override suspend fun saveCreatedCode(
        param: SaveCreatedCodeParam
    ): SaveHistoryResult {
        return try {
            val entity = CreatedCodeEntity(
                content = param.content,
                codeType = param.codeType,
                format = param.format,
                foregroundColor = param.foregroundColor,
                backgroundColor = param.backgroundColor,
                width = param.width,
                height = param.height,
                image = param.logo,
                createdAt = param.createdAt
            )
            val result = historyDao.insertCreatedCode(entity)
            if (result == -1L) {
                SaveHistoryResult.Failure("Unable to save code right now!")
            } else {
                SaveHistoryResult.Saved
            }
        } catch (e: Exception) {
            Timber.e(e)
            SaveHistoryResult.Failure(e.message.toString())
        }
    }

    override suspend fun saveScannedCode(
        param: SaveScannedCodeParam
    ): SaveHistoryResult {
        val entity = ScannedCodeEntity(
            content = param.content,
            bitmap = param.bitmap,
            scannedAt = param.scannedAt
        )
        val result = historyDao.insertScannedCode(entity)
        return if (result == -1L) {
            SaveHistoryResult.Failure("Unable to save code right now!")
        } else {
            SaveHistoryResult.Saved
        }
    }

    override suspend fun deleteCreatedCode(id: Long) {
        historyDao.deleteCreatedCodeById(id)
    }

    override suspend fun deleteScannedCode(id: Long) {
        historyDao.deleteScannedCodeById(id)
    }

    override suspend fun clearHistory(): Int {
        return historyDao.clearAllHistory()
    }

}