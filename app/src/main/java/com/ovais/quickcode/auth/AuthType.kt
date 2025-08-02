package com.ovais.quickcode.auth

const val GOOGLE = "Google"
const val UNKNOWN = "Unknown"

sealed interface AuthType {

    val name: String

    data object Google : AuthType {
        override val name: String
            get() = Google::class.simpleName ?: GOOGLE
    }

    data object Unknown : AuthType {
        override val name: String
            get() = Unknown::class.simpleName ?: UNKNOWN
    }

    companion object {
        fun get(type: String): AuthType {
            return when (type) {
                GOOGLE -> Google
                else -> Unknown
            }
        }
    }
}