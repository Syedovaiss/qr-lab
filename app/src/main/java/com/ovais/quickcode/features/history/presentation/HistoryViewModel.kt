package com.ovais.quickcode.features.history.presentation

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovais.quickcode.features.history.data.HistoryAction
import com.ovais.quickcode.features.history.data.HistoryFilter
import com.ovais.quickcode.features.history.data.HistoryItem
import com.ovais.quickcode.features.history.data.HistoryTab
import com.ovais.quickcode.features.history.domain.DeleteCodeParam
import com.ovais.quickcode.features.history.domain.DeleteCodeUseCase
import com.ovais.quickcode.features.history.domain.GetCreatedCodesUseCase
import com.ovais.quickcode.features.history.domain.GetScannedCodesUseCase
import com.ovais.quickcode.features.history.domain.HistoryType
import com.ovais.quickcode.utils.EMPTY_STRING
import com.ovais.quickcode.utils.KeyValue
import com.ovais.quickcode.utils.clipboard.ClipboardManager
import com.ovais.quickcode.utils.file.FileManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HistoryState(
    val currentTab: Int = 0,
    val createdCodes: List<HistoryItem> = emptyList(),
    val scannedCodes: List<HistoryItem> = emptyList(),
    val filter: HistoryFilter = HistoryFilter(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HistoryViewModel(
    private val getCreatedCodesUseCase: GetCreatedCodesUseCase,
    private val getScannedCodesUseCase: GetScannedCodesUseCase,
    private val deleteCodeUseCase: DeleteCodeUseCase,
    private val clipboardManager: ClipboardManager,
    private val fileManager: FileManager
) : ViewModel() {

    private companion object {
        private const val URL = "url"
        private const val COPIED = "Copied!"
    }

    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state.asStateFlow()

    private val _openURL by lazy { MutableSharedFlow<String>() }
    val openURL: SharedFlow<String>
        get() = _openURL

    private val _shareItem by lazy { MutableSharedFlow<Uri>() }
    val shareItem: SharedFlow<Uri>
        get() = _shareItem

    fun initialize() {
        fetchData()
    }
    private fun fetchScannedCodes() {
        viewModelScope.launch {
            val items = getScannedCodesUseCase()
            _state.update {
                it.copy(
                    scannedCodes = items
                )
            }
        }
    }

    private fun fetchData() {
        fetchCreatedCodes()
        fetchScannedCodes()
    }

    private fun fetchCreatedCodes() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    createdCodes = getCreatedCodesUseCase()
                )
            }
        }
    }

    fun onAction(action: HistoryAction) {
        when (action) {
            is HistoryAction.Refresh -> {
                fetchData()
            }

            is HistoryAction.DeleteItem -> {
                deleteItem(action.id, action.tab)
            }

            is HistoryAction.ShareItem -> {
                shareContent(action.item)
            }

            is HistoryAction.CopyToClipboard -> {
                copyToClipboard(action.content)
            }

            is HistoryAction.OpenUrl -> {
                openURL(action.url)
            }

            is HistoryAction.SwitchTab -> {
                switchTab(action.tab)
            }
        }
    }

    private fun shareContent(item: HistoryItem) {
        viewModelScope.launch {
            val uri = item.logo?.let {
                fileManager.getContentUri(it)
            } ?: run {
                item.displayContent.toUri()
            }

            _shareItem.emit(uri)
        }
    }

    private fun openURL(content: List<KeyValue>) {
        val url = content.find { it.key == URL }
        viewModelScope.launch { _openURL.emit(url?.value ?: EMPTY_STRING) }
    }

    private fun deleteItem(id: Long, tab: HistoryTab) {
        viewModelScope.launch {
            try {
                when (tab) {
                    HistoryTab.CREATED -> deleteCodeUseCase(
                        DeleteCodeParam(
                            id,
                            HistoryType.Created
                        )
                    )

                    HistoryTab.SCANNED -> deleteCodeUseCase(
                        DeleteCodeParam(
                            id,
                            HistoryType.Scanned
                        )
                    )
                }
                fetchData()
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = e.message ?: "Failed to delete item")
                }
            }
        }
    }

    private fun copyToClipboard(content: List<KeyValue>) {
        viewModelScope.launch {
            val item = content.joinToString { it.value }
            try {
                clipboardManager.copy(COPIED, item)
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = e.message ?: "Failed to copy to clipboard")
                }
            }
        }
    }


    private fun switchTab(tab: HistoryTab) {
        val tabIndex = if (tab == HistoryTab.CREATED) 0 else 1
        if (tab == HistoryTab.CREATED) {
            fetchCreatedCodes()
        } else {
            fetchScannedCodes()
        }
        _state.update { it.copy(currentTab = tabIndex) }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
} 