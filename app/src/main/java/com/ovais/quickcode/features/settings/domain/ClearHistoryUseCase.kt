package com.ovais.quickcode.features.settings.domain

import com.ovais.quickcode.features.history.data.HistoryRepository
import com.ovais.quickcode.utils.usecase.SuspendUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext


sealed interface ClearHistoryResult {
    data object Cleared : ClearHistoryResult
    data class Failure(val message: String) : ClearHistoryResult
}

interface ClearHistoryUseCase : SuspendUseCase<ClearHistoryResult>

class DefaultClearHistoryUseCase(
    private val historyRepository: HistoryRepository,
    private val dispatcherIO: CoroutineDispatcher
) : ClearHistoryUseCase {
    override suspend fun invoke(): ClearHistoryResult {
        return withContext(dispatcherIO) {
            val result = historyRepository.clearHistory()
            if (result > 0) {
                ClearHistoryResult.Cleared
            } else {
                ClearHistoryResult.Failure("Failed to delete history!")
            }
        }
    }
}