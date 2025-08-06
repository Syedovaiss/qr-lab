package com.ovais.quickcode.features.create.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ovais.quickcode.core.ui.theme.ButtonDisabled
import com.ovais.quickcode.core.ui.theme.ColorSecondary
import com.ovais.quickcode.features.create.data.CodeItem
import com.ovais.quickcode.features.create.data.CodeType


@Composable
fun CodeTypeSelector(
    codeItems: List<CodeItem>,
    selectedType: CodeType?,
    isQRCode: Boolean,
    onSelected: (CodeItem) -> Unit
) {

    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(220.dp)
    ) {
        items(codeItems) { item ->
            val interactionSource = remember { MutableInteractionSource() }
            val isSelected = item.type == selectedType
            val isDisabled = !isQRCode && item.type != CodeType.Text

            Card(
                modifier = Modifier
                    .size(width = 140.dp, height = 100.dp)
                    .then(
                        if (!isDisabled) Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = rememberRipple()
                        ) { onSelected(item) }
                        else Modifier
                    )
                    .border(
                        width = 2.dp,
                        color = when {
                            isSelected -> ColorSecondary
                            isDisabled -> ButtonDisabled
                            else -> Color.White
                        },
                        shape = RoundedCornerShape(12.dp)
                    ),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDisabled) ButtonDisabled else Color.White
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .alpha(if (isDisabled) 0.4f else 1f) // visually dim the content
                ) {
                    Image(
                        painter = painterResource(id = item.icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(6.dp))
                    )
                }
            }
        }
    }
}
