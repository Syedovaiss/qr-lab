package com.ovais.qrlab.features.create.data

import com.ovais.qrlab.utils.inputs.InputType

data class FieldMetaData(
    val name: String,
    val hint: String,
    val inputType: InputType
)
