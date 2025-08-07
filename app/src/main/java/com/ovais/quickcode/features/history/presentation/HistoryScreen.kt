package com.ovais.quickcode.features.history.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ovais.quickcode.R
import com.ovais.quickcode.core.ui.theme.ErrorColor
import com.ovais.quickcode.features.history.data.HistoryAction
import com.ovais.quickcode.features.history.data.HistoryTab
import com.ovais.quickcode.utils.components.BodyText
import com.ovais.quickcode.utils.components.SubtitleText
import com.ovais.quickcode.utils.components.TopBar
import com.ovais.quickcode.utils.openURL
import com.ovais.quickcode.utils.shareIntent
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onBack: () -> Unit,
    viewModel: HistoryViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.initialize()
    }
    LaunchedEffect(Unit) {
        viewModel.openURL.collectLatest {
            context.openURL(it)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.shareItem.collectLatest {
            context.shareIntent(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar(
            title = R.string.history,
            onBack = onBack
        )

        HistoryTabs(
            options = listOf("Created", "Scanned"),
            selectedIndex = state.currentTab,
            onSelect = { index ->
                val tab = if (index == 0) HistoryTab.CREATED else HistoryTab.SCANNED
                viewModel.onAction(HistoryAction.SwitchTab(tab))
            }
        )

        when {
            state.isLoading -> {
                HistoryLoadingView()
            }

            state.error != null -> {
                ErrorState(
                    error = state.error.orEmpty(),
                    onRetry = { viewModel.onAction(HistoryAction.Refresh) },
                    onDismiss = { viewModel.clearError() }
                )
            }

            else -> {
                HistoryContent(
                    state = state,
                    onAction = viewModel::onAction
                )
            }
        }
    }
}


@Composable
private fun HistoryContent(
    state: HistoryState,
    onAction: (HistoryAction) -> Unit
) {
    val items = if (state.currentTab == 0) state.createdCodes else state.scannedCodes

    if (items.isEmpty()) {
        HistoryEmptyView()
    } else {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(
                items = items,
                key = { it.id }
            ) { item ->
                HistoryItemCard(
                    item = item,
                    onDelete = {
                        val tab =
                            if (state.currentTab == 0) HistoryTab.CREATED else HistoryTab.SCANNED
                        onAction(HistoryAction.DeleteItem(item.id, tab))
                    },
                    onShare = {
                        onAction(HistoryAction.ShareItem(item))
                    },
                    onCopy = {
                        onAction(HistoryAction.CopyToClipboard(item.content))
                    },
                    onOpenUrl = {
                        onAction(HistoryAction.OpenUrl(item.content))
                    }
                )
            }
        }
    }
}

@Composable
private fun ErrorState(
    error: String,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                SubtitleText(
                    text = "Unable to Load History",
                    textColor = ErrorColor,
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss",
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
            BodyText(
                text = error
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onRetry) {
                SubtitleText("Retry")
            }
        }
    }
}