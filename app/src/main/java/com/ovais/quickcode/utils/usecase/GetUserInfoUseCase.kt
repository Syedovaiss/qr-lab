package com.ovais.quickcode.utils.usecase

import android.content.Context
import com.ovais.quickcode.R
import com.ovais.quickcode.auth.AuthType
import com.ovais.quickcode.features.home.data.UserInfo
import com.ovais.quickcode.storage.QuickCodePreferenceManager
import com.ovais.quickcode.utils.GOOGLE_PHOTO_BASE_URL
import com.ovais.quickcode.utils.InitialProvider
import com.ovais.quickcode.utils.StringResourceProvider
import com.ovais.quickcode.utils.orEmpty
import com.ovais.quickcode.utils.orFalse

interface GetUserInfoUseCase : SuspendUseCase<UserInfo>

class DefaultGetUserInfoUseCase(
    private val context: Context,
    private val preferenceManager: QuickCodePreferenceManager,
    private val initialProvider: InitialProvider,
    private val stringResourceProvider: StringResourceProvider
) : GetUserInfoUseCase {
    private companion object {
        private const val IS_LOGGED_IN = "is_logged_in"
        private const val AVATAR = "avatar_url"
        private const val NAME = "name"
        private const val AUTH_TYPE = "auth_type"
    }

    override suspend fun invoke(): UserInfo {
        val isLoggedIn = preferenceManager.read(context, IS_LOGGED_IN, Boolean::class.java).orFalse
        return if (isLoggedIn.not()) {
            val name = stringResourceProvider.get(R.string.default_name)
            UserInfo(
                name = name,
                initials = initialProvider(name)
            )
        } else {
            val name = preferenceManager.read(context, NAME, String::class.java).orEmpty
            val avatar = preferenceManager.read(context, AVATAR, String::class.java).orEmpty
            val authType = preferenceManager.read(context, AUTH_TYPE, String::class.java).orEmpty
            UserInfo(
                avatar = getAvatar(avatar, authType),
                name = name,
                initials = initialProvider(name)
            )
        }
    }

    private fun getAvatar(imageUrl: String, authType: String): String {
        return if (AuthType.get(authType) is AuthType.Google) {
            buildString {
                append(GOOGLE_PHOTO_BASE_URL)
                append(imageUrl)
            }
        } else imageUrl
    }
}