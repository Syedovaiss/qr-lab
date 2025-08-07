package com.ovais.quickcode.features.history.data

import android.graphics.Bitmap
import com.ovais.quickcode.features.create.data.CodeFormats
import com.ovais.quickcode.features.create.data.CodeType
import com.ovais.quickcode.utils.KeyValue
import java.time.LocalDateTime
import java.util.Locale

data class HistoryItem(
    val id: Long,
    val content: List<KeyValue>,
    val codeType: CodeType,
    val format: CodeFormats,
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

enum class SortOrder {
    NEWEST_FIRST, OLDEST_FIRST
}

enum class FilterType {
    ALL
}

data class HistoryFilter(
    val sortOrder: SortOrder = SortOrder.NEWEST_FIRST,
    val filterType: FilterType = FilterType.ALL,
    val searchQuery: String = "",
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null
)

sealed class HistoryAction {
    data object Refresh : HistoryAction()
    data class DeleteItem(val id: Long, val tab: HistoryTab) : HistoryAction()
    data class ShareItem(val item: HistoryItem) : HistoryAction()
    data class CopyToClipboard(val content: List<KeyValue>) : HistoryAction()
    data class OpenUrl(val url: List<KeyValue>) : HistoryAction()
    data class SwitchTab(val tab: HistoryTab) : HistoryAction()
} 