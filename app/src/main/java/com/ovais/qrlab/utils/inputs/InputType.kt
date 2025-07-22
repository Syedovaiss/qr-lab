package com.ovais.qrlab.utils.inputs

sealed interface InputType {
    data object Text : InputType
    data object Phone : InputType
    data object Email : InputType
    data object Number : InputType
    data object Url : InputType
    data object Password : InputType
    data object DateTime : InputType
    data object Location : InputType
}