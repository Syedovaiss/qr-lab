package com.ovais.quickcode.utils.clipboard

import android.content.ClipData
import android.content.Context

fun interface ClipboardManager {
    fun copy(label: String, text: String)
}

class DefaultClipboardManager(
    private val context: Context
) : ClipboardManager {
    override fun copy(label: String, text: String) {
        val clipboard =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
    }
}