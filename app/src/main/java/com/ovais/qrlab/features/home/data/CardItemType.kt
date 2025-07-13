package com.ovais.qrlab.features.home.data

sealed interface CardItemType {
    data object Scan : CardItemType
    data object Create : CardItemType
    data object History : CardItemType
    data object Settings : CardItemType
}