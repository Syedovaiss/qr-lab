package com.ovais.quickcode.features.code_details.domain

sealed interface SaveImageResult {
    data object Saved : SaveImageResult
    data class Failure(val message: String) : SaveImageResult
}