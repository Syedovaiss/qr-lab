package com.ovais.quickcode.features.create.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class CodeItem(
    @DrawableRes val icon: Int,
    val type: CodeType,
    @StringRes val title: Int,
    val fieldMetaData: List<FieldMetaData>
)
