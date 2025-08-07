package com.ovais.quickcode.features.history.domain

import com.ovais.quickcode.features.history.data.HistoryFilter
import com.ovais.quickcode.features.history.data.HistoryItem
import com.ovais.quickcode.features.history.data.HistoryRepository
import com.ovais.quickcode.utils.usecase.SuspendParameterizedUseCase
import kotlinx.coroutines.flow.Flow

interface GetScannedCodesUseCase :
    SuspendParameterizedUseCase<HistoryFilter, Flow<List<HistoryItem>>>

class DefaultGetScannedCodesUseCase(
    private val repository: HistoryRepository
) : GetScannedCodesUseCase {
    override suspend fun invoke(param: HistoryFilter): Flow<List<HistoryItem>> {
        return repository.getScannedCodes(param)
    }

}