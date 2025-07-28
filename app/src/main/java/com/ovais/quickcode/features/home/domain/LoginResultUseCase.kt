package com.ovais.quickcode.features.home.domain

import android.content.Context
import com.ovais.quickcode.features.home.data.AuthState
import com.ovais.quickcode.features.home.data.LoggedInParam
import com.ovais.quickcode.storage.QuickCodePreferenceManager
import com.ovais.quickcode.utils.usecase.SuspendParameterizedUseCase

interface LoginResultUseCase : SuspendParameterizedUseCase<LoggedInParam, AuthState>

class DefaultLoginResultUseCase(
    private val context: Context,
    private val preferenceManager: QuickCodePreferenceManager
) : LoginResultUseCase {
    private companion object {
        private const val AVATAR = "avatar_url"
        private const val NAME = "name"
        private const val IS_LOGGED_IN = "is_logged_in"
        private const val EMAIL = "email"
        private const val AUTH_TYPE = "auth_type"
    }

    override suspend fun invoke(param: LoggedInParam): AuthState {
        param.avatar?.let {
            if (it.isNotBlank()) {
                preferenceManager.save(context, AVATAR, it)
            }
        }
        param.name?.let {
            preferenceManager.save(context, NAME, it)
        }
        param.email?.let {
            preferenceManager.save(context, EMAIL, it)
            preferenceManager.save(context, IS_LOGGED_IN, true)
        }
        preferenceManager.save(context, AUTH_TYPE, param.type.name)
        return AuthState.LoggedIn
    }
}