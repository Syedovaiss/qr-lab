package com.ovais.quickcode.utils.usecase

import android.graphics.Bitmap
import android.net.Uri
import com.ovais.quickcode.utils.file.FileManager

interface GetContentUriUseCase : SuspendParameterizedUseCase<Bitmap?, ContentResult>

class DefaultGetContentUriUseCase(
    private val fileManager: FileManager
) : GetContentUriUseCase {
    override suspend fun invoke(param: Bitmap?): ContentResult {
        if (param == null || param.isRecycled) {
            return ContentResult.Failure("Invalid Image")
        }
        return ContentResult.Success(
            fileManager.getContentUri(param)
        )
    }
}

sealed interface ContentResult {
    data class Success(val uri: Uri) : ContentResult
    data class Failure(val message: String) : ContentResult
}