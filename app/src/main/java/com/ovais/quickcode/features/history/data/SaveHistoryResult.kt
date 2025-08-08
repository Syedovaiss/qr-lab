package com.ovais.quickcode.features.history.data

sealed interface SaveHistoryResult {
    data object Saved : SaveHistoryResult
    data class Failure(val message: String) : SaveHistoryResult
}