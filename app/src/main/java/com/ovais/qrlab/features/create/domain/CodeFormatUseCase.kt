package com.ovais.qrlab.features.create.domain

import com.ovais.qrlab.features.create.data.CodeFormats
import com.ovais.qrlab.utils.usecase.UseCase

interface CodeFormatUseCase : UseCase<MutableList<CodeFormats>>

class DefaultCodeFormatUseCase : CodeFormatUseCase {
    override fun invoke(): MutableList<CodeFormats> {
        return CodeFormats.formats
    }
}