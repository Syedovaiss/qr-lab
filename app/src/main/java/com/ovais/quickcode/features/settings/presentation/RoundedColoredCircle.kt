package com.ovais.quickcode.features.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
@Composable
fun RoundedColoredCircle(
    color: Color,
    size: Dp = 32.dp, // Smaller overall size
    strokeWidth: Dp = 1.dp,
    strokeColor: Color = Color.LightGray,
    isSelected: Boolean = false,
    selectedStrokeWidth: Dp = 2.dp,
    selectedStrokeColor: Color = Color.Black,
    paddingBetweenStrokeAndCircle: Dp = 2.dp,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .size(size)
            .border(
                width = if (isSelected) selectedStrokeWidth else strokeWidth,
                color = if (isSelected) selectedStrokeColor else strokeColor,
                shape = CircleShape
            )
            .clip(CircleShape)
            .background(Color.White)
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(bounded = true, radius = size / 2)
            ) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(size - (paddingBetweenStrokeAndCircle * 2))
                .clip(CircleShape)
                .background(color)
        )
    }
}