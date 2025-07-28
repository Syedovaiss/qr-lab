package com.ovais.quickcode.features.create.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class CodeItem(
    @param:DrawableRes val icon: Int,
    val type: CodeType,
    @param:StringRes val title: Int,
    val fieldMetaData: List<FieldMetaData>
)
