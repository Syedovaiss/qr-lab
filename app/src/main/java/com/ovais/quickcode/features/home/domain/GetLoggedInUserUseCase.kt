package com.ovais.quickcode.features.home.domain

import android.content.Context
import com.ovais.quickcode.features.home.data.UserInfo
import com.ovais.quickcode.storage.QuickCodePreferenceManager
import com.ovais.quickcode.utils.InitialProvider
import com.ovais.quickcode.utils.orEmpty
import com.ovais.quickcode.utils.usecase.SuspendUseCase

interface GetLoggedInUserUseCase : SuspendUseCase<UserInfo>

class DefaultGetLoggedInUserUseCase(
    private val context: Context,
    private val preferenceManager: QuickCodePreferenceManager,
    private val initialProvider: InitialProvider
) : GetLoggedInUserUseCase {
    private companion object {
        private const val AVATAR = "avatar_url"
        private const val NAME = "name"
    }

    override suspend fun invoke(): UserInfo {
        val avatar: String? = preferenceManager.read(context, AVATAR, String::class.java)
        val name: String? = preferenceManager.read(context, NAME, String::class.java)
        val initials = name?.let {
            initialProvider(name)
        }
        return UserInfo(
            avatar,
            name.orEmpty,
            initials.orEmpty
        )
    }
}