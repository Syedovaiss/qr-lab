package com.ovais.quickcode.utils.usecase

import android.graphics.Bitmap
import android.net.Uri
import com.ovais.quickcode.features.code_details.domain.SaveImageResult
import com.ovais.quickcode.utils.QRFormat
import com.ovais.quickcode.utils.file.FileManager
import com.ovais.quickcode.utils.file.FileResult
import com.ovais.quickcode.utils.orFalse

interface SaveImageUseCase :
    SuspendParameterizedUseCase<Triple<Bitmap?, Uri, String>, SaveImageResult>

class DefaultSaveImageUseCase(
    private val fileManager: FileManager,
) : SaveImageUseCase {

    override suspend fun invoke(param: Triple<Bitmap?, Uri, String>): SaveImageResult {
        if (param.first == null || param.first?.isRecycled.orFalse) {
            return SaveImageResult.Failure("Invalid Image")
        }
        val format = if (param.third.contains("JPG")) QRFormat.JPG else QRFormat.PNG
        return when (val result = fileManager.saveImage(param.first!!, param.second, format)) {
            is FileResult.Success -> SaveImageResult.Saved
            is FileResult.Failure -> SaveImageResult.Failure(result.message)
        }
    }
}