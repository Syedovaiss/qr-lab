package com.ovais.quickcode.features.history.presentation

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.res.painterResource
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
    onOpenUrl: () -> Unit,
    onSaveImage: (Bitmap?) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(200.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 48.dp) // Reserve space for menu
            ) {
                // Colored left strip
                Box(
                    modifier = Modifier
                        .width(6.dp)
                        .fillMaxHeight()
                        .background(SelectedTabColor)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Logo
                item.logo?.let { bitmap ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .align(Alignment.CenterVertically)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Text content
                Column(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    BodyText(item.displayContent)
                    Spacer(modifier = Modifier.height(8.dp))
                    BodyText(item.timestamp)
                }
            }

            // More menu (top right)
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                IconButton(onClick = { showMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null
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
                            Image(
                                painter = painterResource(R.drawable.ic_copy),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        onClick = {
                            onCopy()
                            showMenu = false
                        }
                    )

                    DropdownMenuItem(
                        text = { BodyText(stringResource(R.string.share)) },
                        leadingIcon = {
                            Image(
                                painter = painterResource(R.drawable.ic_share),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        onClick = {
                            onShare()
                            showMenu = false
                        }
                    )

                    if (item.codeType is CodeType.Website) {
                        DropdownMenuItem(
                            text = { BodyText(stringResource(R.string.web_url)) },
                            leadingIcon = {
                                Image(
                                    painter = painterResource(R.drawable.ic_website),
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            onClick = {
                                onOpenUrl()
                                showMenu = false
                            }
                        )
                    }
                    DropdownMenuItem(
                        text = { BodyText(stringResource(R.string.download)) },
                        leadingIcon = {
                            Image(
                                painter = painterResource(R.drawable.ic_save),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        onClick = {
                            onSaveImage(item.logo)
                            showMenu = false
                        }
                    )

                    DropdownMenuItem(
                        text = { BodyText(stringResource(R.string.delete)) },
                        leadingIcon = {
                            Image(
                                painter = painterResource(R.drawable.ic_delete),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        onClick = {
                            onDelete()
                            showMenu = false
                        }
                    )
                }
            }
        }
    }
}