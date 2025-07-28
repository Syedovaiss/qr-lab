package com.ovais.quickcode.features.home.data

sealed interface AuthState {
    data object LoggedIn : AuthState
    data class Failure(val message: String) : AuthState
}