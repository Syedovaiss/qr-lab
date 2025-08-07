package com.ovais.quickcode.features.history.domain

import com.ovais.quickcode.features.history.data.HistoryRepository
import com.ovais.quickcode.utils.usecase.SuspendParameterizedUseCase

sealed interface HistoryType {
    data object Scanned : HistoryType
    data object Created : HistoryType
}

data class DeleteCodeParam(
    val id: Long,
    val type: HistoryType
)

interface DeleteCodeUseCase : SuspendParameterizedUseCase<DeleteCodeParam, Unit>

class DefaultDeleteCreatedCodeUseCase(
    private val repository: HistoryRepository
) : DeleteCodeUseCase {
    override suspend fun invoke(param: DeleteCodeParam) {
        if (param.type is HistoryType.Created) {
            repository.deleteCreatedCode(param.id)
        } else {
            repository.deleteScannedCode(param.id)
        }
    }
}
