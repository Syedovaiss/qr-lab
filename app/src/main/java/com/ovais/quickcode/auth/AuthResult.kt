package com.ovais.quickcode.auth

sealed interface AuthResult {
    data class Success(
        val userId: String? = null,
        val token: String? = null,
        val email: String? = null,
        val avatar: String? = null,
        val name: String? = null
    ) : AuthResult

    data class Failure(val message: String) : AuthResult
}