package com.ovais.quickcode.features.create.data

sealed interface CodeType {
    data object Text : CodeType
    data object Website : CodeType
    data object Instagram : CodeType
    data object Facebook : CodeType
    data object WhatsApp : CodeType
    data object YouTube : CodeType
    data object Email : CodeType
    data object Search : CodeType
    data object Threads : CodeType
    data object Discord : CodeType
    data object SMS : CodeType
    data object Phone : CodeType
    data object LinkedIn : CodeType
    data object GeoLocation : CodeType
    data object PayPal : CodeType
    data object Bitcoin : CodeType
    data object Zoom : CodeType
    data object Snapchat : CodeType
}