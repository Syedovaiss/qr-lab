package com.ovais.quickcode.features.home.data

import com.ovais.quickcode.auth.AuthType

data class LoggedInParam(
    val email: String?,
    val name: String?,
    val avatar: String?,
    val type: AuthType
)
