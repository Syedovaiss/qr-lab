package com.ovais.quickcode.features.create.domain

import com.ovais.quickcode.features.create.data.CodeFormats
import com.ovais.quickcode.utils.usecase.UseCase

interface CodeFormatUseCase : UseCase<MutableList<CodeFormats>>

class DefaultCodeFormatUseCase : CodeFormatUseCase {
    override fun invoke(): MutableList<CodeFormats> {
        return CodeFormats.formats
    }
}