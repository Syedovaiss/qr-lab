package com.ovais.quickcode.features.create.data

import com.ovais.quickcode.utils.inputs.InputType

data class FieldMetaData(
    val name: String,
    val hint: String,
    val inputType: InputType
)
