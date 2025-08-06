package com.ovais.quickcode.features.create.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ovais.quickcode.R
import com.ovais.quickcode.core.ui.theme.colorsForColorPicker
import com.ovais.quickcode.utils.components.BodyText
import com.ovais.quickcode.utils.components.ColorPickerDialog
import com.ovais.quickcode.utils.components.SubtitleText


@Composable
fun ForegroundColorPicker(
    colorSelected: (Color) -> Unit
) {
    Column {
        var selectedColor by remember { mutableStateOf<Color?>(null) }
        val interactionSource = remember { MutableInteractionSource() }
        var canShowColorPicker by remember { mutableStateOf(false) }
        SubtitleText(
            stringResource(R.string.select_foreground_color),
            paddingValues = PaddingValues(16.dp)
        )
        BodyText(
            text = stringResource(R.string.default_color_description),
            paddingValues = PaddingValues(horizontal = 16.dp),
            color = Color.Gray,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp
        )
        selectedColor?.let { color ->
            val colorInteractionSource = remember { MutableInteractionSource() }
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color)
                    .clickable(
                        interactionSource = colorInteractionSource,
                        indication = LocalIndication.current
                    ) {
                        canShowColorPicker = canShowColorPicker.not()
                    }
            )
        } ?: run {
            Image(
                painter = painterResource(R.drawable.ic_add_square),
                contentDescription = null,
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .padding(16.dp)
                    .clickable(
                        interactionSource,
                        LocalIndication.current
                    ) {
                        canShowColorPicker = canShowColorPicker.not()
                    }
            )
        }
        if (canShowColorPicker) {
            ColorPickerDialog(
                title = stringResource(R.string.select_color),
                colors = colorsForColorPicker,
                selectedColor = selectedColor ?: Color.Black,
                onColorSelected = {
                    selectedColor = it
                    colorSelected(it)
                    canShowColorPicker = false
                },
                onDismiss = {
                    canShowColorPicker = false
                }
            )
        }
    }
}