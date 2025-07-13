package com.ovais.qrlab.features.home.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color

data class HomeCardItem(
    @DrawableRes val iconRes: Int,
    @StringRes val title: Int,
    val type: CardItemType,
    val gradientColors: List<Color>,
    val titleColor: Color = Color.White
)
