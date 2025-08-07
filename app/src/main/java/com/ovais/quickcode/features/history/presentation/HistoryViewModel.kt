package com.ovais.quickcode.features.history.presentation

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
import com.ovais.quickcode.utils.clipboard.ClipboardManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
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
    private val clipboardManager: ClipboardManager
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state.asStateFlow()

    private val _actions = MutableSharedFlow<HistoryAction>()
    val actions: SharedFlow<HistoryAction> = _actions.asSharedFlow()

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            combine(
                getCreatedCodesUseCase(state.value.filter),
                getScannedCodesUseCase(state.value.filter)
            ) { createdCodes, scannedCodes ->
                _state.update {
                    it.copy(
                        createdCodes = createdCodes,
                        scannedCodes = scannedCodes
                    )
                }
            }.collect()
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
                _actions.tryEmit(action)
            }

            is HistoryAction.CopyToClipboard -> {
                copyToClipboard(action.content.joinToString())
            }

            is HistoryAction.OpenUrl -> {
                _actions.tryEmit(action)
            }

            is HistoryAction.SwitchTab -> {
                switchTab(action.tab)
            }
        }
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

    private fun copyToClipboard(content: String) {
        viewModelScope.launch {
            try {
                clipboardManager.copy("Copied", content)
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = e.message ?: "Failed to copy to clipboard")
                }
            }
        }
    }


    private fun switchTab(tab: HistoryTab) {
        val tabIndex = if (tab == HistoryTab.CREATED) 0 else 1
        _state.update { it.copy(currentTab = tabIndex) }
        fetchData()
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
} 