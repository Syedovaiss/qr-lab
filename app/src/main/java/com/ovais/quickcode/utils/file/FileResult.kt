package com.ovais.quickcode.utils.file

sealed interface FileResult {
    data object Success : FileResult
    data class Failure(val message: String) : FileResult
}