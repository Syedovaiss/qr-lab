package com.ovais.quickcode.utils

import androidx.annotation.StringRes

interface Provider<T> {
    fun get(@StringRes resId: Int): T
}