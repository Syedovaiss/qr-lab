package com.ovais.quickcode.features.create.data

import android.graphics.Bitmap

sealed interface CodeResult {
    data class Success(val code: Bitmap) : CodeResult
    data class Failure(val message: String) : CodeResult
}