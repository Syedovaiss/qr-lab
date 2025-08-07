package com.ovais.quickcode.features.history.domain

import com.ovais.quickcode.features.history.data.HistoryItem
import com.ovais.quickcode.features.history.data.HistoryRepository
import com.ovais.quickcode.utils.usecase.SuspendUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface GetScannedCodesUseCase : SuspendUseCase<List<HistoryItem>>

class DefaultGetScannedCodesUseCase(
    private val repository: HistoryRepository,
    private val dispatcherIO: CoroutineDispatcher
) : GetScannedCodesUseCase {
    override suspend fun invoke(): List<HistoryItem> {
        return withContext(dispatcherIO) {
            repository.getScannedCodes()
        }
    }

}