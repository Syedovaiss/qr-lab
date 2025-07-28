package com.ovais.quickcode.features.home.domain

import android.content.Context
import com.ovais.quickcode.R
import com.ovais.quickcode.features.home.data.UserInfo
import com.ovais.quickcode.storage.QuickCodePreferenceManager
import com.ovais.quickcode.utils.InitialProvider
import com.ovais.quickcode.utils.orEmpty
import com.ovais.quickcode.utils.orFalse
import com.ovais.quickcode.utils.usecase.SuspendUseCase

interface GetUserInfoUseCase : SuspendUseCase<UserInfo>

class DefaultGetUserInfoUseCase(
    private val context: Context,
    private val preferenceManager: QuickCodePreferenceManager,
    private val initialProvider: InitialProvider
) : GetUserInfoUseCase {
    private companion object {
        private const val IS_LOGGED_IN = "is_logged_in"
        private const val FIRST_NAME = "first_name"
        private const val LAST_NAME = "last_name"
        private const val AVATAR = "avatar_url"
    }

    override suspend fun invoke(): UserInfo {
        val isLoggedIn = preferenceManager.read(context, IS_LOGGED_IN, Boolean::class.java).orFalse
        return if (isLoggedIn.not()) {
            val name = context.getString(R.string.default_name)
            UserInfo(
                name = name,
                initials = initialProvider(name)
            )
        } else {
            val firstName = preferenceManager.read(context, FIRST_NAME, String::class.java).orEmpty
            val lastName = preferenceManager.read(context, LAST_NAME, String::class.java).orEmpty
            val fullName = "$firstName $lastName"
            val avatar = preferenceManager.read(context, AVATAR, String::class.java).orEmpty
            UserInfo(
                avatar = avatar,
                name = fullName,
                initials = initialProvider(fullName)
            )
        }
    }
}