package com.ovais.quickcode.features.create.domain

import com.ovais.quickcode.features.history.data.HistoryRepository
import com.ovais.quickcode.features.history.data.SaveCreatedCodeParam
import com.ovais.quickcode.features.history.data.SaveHistoryResult
import com.ovais.quickcode.utils.usecase.SuspendParameterizedUseCase

sealed interface SaveCodeResult {
    data object Saved : SaveCodeResult
    data class Failure(val message: String) : SaveCodeResult
}

interface SaveCreatedCodeUseCase : SuspendParameterizedUseCase<SaveCreatedCodeParam, SaveCodeResult>

class DefaultSaveCreatedCodeUseCase(
    private val repository: HistoryRepository
) : SaveCreatedCodeUseCase {
    override suspend fun invoke(param: SaveCreatedCodeParam): SaveCodeResult {
        return when (val result = repository.saveCreatedCode(param)) {
            is SaveHistoryResult.Saved -> SaveCodeResult.Saved
            is SaveHistoryResult.Failure -> SaveCodeResult.Failure(result.message)
        }
    }
}