package com.ovais.quickcode.features.history.domain

import com.ovais.quickcode.features.history.data.HistoryFilter
import com.ovais.quickcode.features.history.data.HistoryItem
import com.ovais.quickcode.features.history.data.HistoryRepository
import com.ovais.quickcode.utils.usecase.SuspendParameterizedUseCase
import kotlinx.coroutines.flow.Flow

interface GetCreatedCodesUseCase :
    SuspendParameterizedUseCase<HistoryFilter, Flow<List<HistoryItem>>>

class DefaultGetCreatedCodesUseCase(
    private val repository: HistoryRepository
) : GetCreatedCodesUseCase {
    override suspend fun invoke(param: HistoryFilter): Flow<List<HistoryItem>> {
        return repository.getCreatedCodes(param)
    }

}
