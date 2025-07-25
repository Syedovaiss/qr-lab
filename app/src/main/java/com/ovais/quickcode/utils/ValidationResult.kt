package com.ovais.quickcode.utils

sealed interface ValidationResult {
    data object Valid: ValidationResult
    data class InValid(val message: String): ValidationResult
}