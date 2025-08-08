package com.ovais.quickcode.features.history.data

import android.graphics.Bitmap
import android.net.Uri
import com.ovais.quickcode.features.create.data.CodeFormats
import com.ovais.quickcode.features.create.data.CodeType
import com.ovais.quickcode.utils.KeyValue
import java.util.Locale

data class HistoryItem(
    val id: Long,
    val content: List<KeyValue>,
    val codeType: CodeType? = null,
    val format: CodeFormats? = null,
    val timestamp: String,
    val source: String? = null,
    val foregroundColor: String? = null,
    val backgroundColor: String? = null,
    val width: Int? = null,
    val height: Int? = null,
    val hasLogo: Boolean? = null,
    val logo: Bitmap? = null
) {
    val displayContent: String
        get() = content.joinToString(separator = "\n") { kv ->
            "${kv.key.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}: ${kv.value}"
        }
}

enum class HistoryTab {
    CREATED, SCANNED
}

sealed class HistoryAction {
    data object Refresh : HistoryAction()
    data class DeleteItem(val id: Long, val tab: HistoryTab) : HistoryAction()
    data class ShareItem(val item: HistoryItem) : HistoryAction()
    data class CopyToClipboard(
        val label: String,
        val content: List<KeyValue>
    ) : HistoryAction()

    data class OpenUrl(val url: List<KeyValue>) : HistoryAction()
    data class SwitchTab(val tab: HistoryTab) : HistoryAction()
    data class UpdateDownloadedImage(val bitmap: Bitmap?) : HistoryAction()
    data class DownloadImage(val uri: Uri) : HistoryAction()
} 