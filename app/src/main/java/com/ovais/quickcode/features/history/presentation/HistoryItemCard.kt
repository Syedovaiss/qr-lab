package com.ovais.quickcode.features.history.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ovais.quickcode.R
import com.ovais.quickcode.core.ui.theme.SelectedTabColor
import com.ovais.quickcode.features.create.data.CodeType
import com.ovais.quickcode.features.history.data.HistoryItem
import com.ovais.quickcode.utils.components.BodyText

@Composable
fun HistoryItemCard(
    item: HistoryItem,
    onDelete: () -> Unit,
    onShare: () -> Unit,
    onCopy: () -> Unit,
    onOpenUrl: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(200.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .width(10.dp)
                        .fillMaxHeight()
                        .background(SelectedTabColor)
                )

                Spacer(modifier = Modifier.width(8.dp))
                item.logo?.let { bitmap ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .align(Alignment.CenterVertically)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .weight(1f)
                ) {
                    BodyText(
                        item.displayContent
                    )
                    BodyText(
                        text = "Date:${item.timestamp}"
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                IconButton(onClick = {
                    showMenu = true
                }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options"
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    DropdownMenuItem(
                        text = { BodyText(stringResource(R.string.copy)) },
                        leadingIcon = {
                            Icon(Icons.Outlined.Home, contentDescription = null)
                        },
                        onClick = {
                            onCopy()
                            showMenu = false
                        },
                        modifier = Modifier.background(Color.White)
                    )
                    DropdownMenuItem(
                        text = { BodyText(stringResource(R.string.share)) },
                        leadingIcon = {
                            Icon(Icons.Default.Share, contentDescription = null)
                        },
                        onClick = {
                            onShare()
                            showMenu = false
                        },
                        modifier = Modifier.background(Color.White)
                    )
                    if (item.codeType is CodeType.Website) {
                        DropdownMenuItem(
                            text = { BodyText(stringResource(R.string.web_url)) },
                            leadingIcon = {
                                Icon(Icons.Outlined.Share, contentDescription = null)
                            },
                            onClick = {
                                onOpenUrl()
                                showMenu = false
                            },
                            modifier = Modifier.background(Color.White)
                        )
                    }
                    DropdownMenuItem(
                        text = { BodyText(stringResource(R.string.delete)) },
                        leadingIcon = {
                            Icon(Icons.Default.Delete, contentDescription = null)
                        },
                        onClick = {
                            onDelete()
                            showMenu = false
                        },
                        modifier = Modifier.background(Color.White)
                    )
                }
            }
        }
    }
}
