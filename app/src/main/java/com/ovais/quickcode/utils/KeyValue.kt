package com.ovais.quickcode.utils

import kotlinx.serialization.Serializable

@Serializable
data class KeyValue(
    val key: String,
    val value: String
)
