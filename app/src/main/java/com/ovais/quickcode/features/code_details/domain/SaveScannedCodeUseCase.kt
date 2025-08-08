package com.ovais.quickcode.features.code_details.domain

import com.ovais.quickcode.features.create.domain.SaveCodeResult
import com.ovais.quickcode.features.history.data.HistoryRepository
import com.ovais.quickcode.features.history.data.SaveHistoryResult
import com.ovais.quickcode.features.history.data.SaveScannedCodeParam
import com.ovais.quickcode.utils.usecase.SuspendParameterizedUseCase

interface SaveScannedCodeUseCase : SuspendParameterizedUseCase<SaveScannedCodeParam, SaveCodeResult>

class DefaultSaveScannedCodeUseCase(
    private val repository: HistoryRepository
) : SaveScannedCodeUseCase {
    override suspend fun invoke(param: SaveScannedCodeParam): SaveCodeResult {
        return when (val result = repository.saveScannedCode(param)) {
            is SaveHistoryResult.Saved -> SaveCodeResult.Saved
            is SaveHistoryResult.Failure -> SaveCodeResult.Failure(result.message)
        }
    }
}