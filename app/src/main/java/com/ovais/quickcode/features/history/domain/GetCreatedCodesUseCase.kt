package com.ovais.quickcode.features.history.domain

import com.ovais.quickcode.features.history.data.HistoryItem
import com.ovais.quickcode.features.history.data.HistoryRepository
import com.ovais.quickcode.utils.usecase.SuspendUseCase
import kotlinx.coroutines.flow.Flow

interface GetCreatedCodesUseCase : SuspendUseCase<Flow<List<HistoryItem>>>

class DefaultGetCreatedCodesUseCase(
    private val repository: HistoryRepository
) : GetCreatedCodesUseCase {
    override suspend fun invoke(): Flow<List<HistoryItem>> {
        return repository.getCreatedCodes()
    }

}
