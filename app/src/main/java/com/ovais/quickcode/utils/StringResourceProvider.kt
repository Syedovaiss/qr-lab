package com.ovais.quickcode.utils

import android.content.Context
import java.util.Locale

interface StringResourceProvider : Provider<String>

class DefaultStringResourceProvider(
    private val context: Context
) : StringResourceProvider {
    override fun get(resId: Int): String {
        val ctx = context.wrapLocale(Locale.getDefault())
        return ctx.getString(resId)
    }

    private fun Context.wrapLocale(locale: Locale): Context {
        val config = resources.configuration
        config.setLocale(locale)
        return createConfigurationContext(config)
    }
}