package com.ovais.quickcode.features.settings.domain

import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import com.ovais.quickcode.utils.usecase.ParameterizedUseCase
import com.ovais.quickcode.worker.job.ExportCSVWorker
import com.ovais.quickcode.worker.job.ExportPDFWorker

interface GetWorkRequestUseCase : ParameterizedUseCase<String, OneTimeWorkRequest>

class DefaultGetWorkRequestUseCase : GetWorkRequestUseCase {

    private companion object {
        private const val CSV = "CSV"
    }

    override fun invoke(param: String): OneTimeWorkRequest {
        return if (param.contains(CSV)) {
            OneTimeWorkRequestBuilder<ExportCSVWorker>()
                .build()
        } else {
            OneTimeWorkRequestBuilder<ExportPDFWorker>()
                .build()
        }
    }
}