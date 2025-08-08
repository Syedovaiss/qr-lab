package com.ovais.quickcode.features.create.data

sealed interface CodeType {
    val name: String

    data object Text : CodeType {
        override val name: String = "Text"
    }

    data object Website : CodeType {
        override val name: String = "Website"
    }

    data object Instagram : CodeType {
        override val name: String = "Instagram"
    }

    data object Facebook : CodeType {
        override val name: String = "Facebook"
    }

    data object WhatsApp : CodeType {
        override val name: String = "WhatsApp"
    }

    data object YouTube : CodeType {
        override val name: String = "YouTube"
    }

    data object Email : CodeType {
        override val name: String = "Email"
    }

    data object Search : CodeType {
        override val name: String = "Search"
    }

    data object Threads : CodeType {
        override val name: String = "Threads"
    }

    data object Discord : CodeType {
        override val name: String = "Discord"
    }

    data object SMS : CodeType {
        override val name: String = "SMS"
    }

    data object Phone : CodeType {
        override val name: String = "Phone"
    }

    data object LinkedIn : CodeType {
        override val name: String = "LinkedIn"
    }

    data object GeoLocation : CodeType {
        override val name: String = "GeoLocation"
    }

    data object PayPal : CodeType {
        override val name: String = "PayPal"
    }

    data object Bitcoin : CodeType {
        override val name: String = "Bitcoin"
    }

    data object Zoom : CodeType {
        override val name: String = "Zoom"
    }

    data object Snapchat : CodeType {
        override val name: String = "Snapchat"
    }

    companion object {
        fun fromName(name: String): CodeType? = when (name) {
            "Text" -> Text
            "Website" -> Website
            "Instagram" -> Instagram
            "Facebook" -> Facebook
            "WhatsApp" -> WhatsApp
            "YouTube" -> YouTube
            "Email" -> Email
            "Search" -> Search
            "Threads" -> Threads
            "Discord" -> Discord
            "SMS" -> SMS
            "Phone" -> Phone
            "LinkedIn" -> LinkedIn
            "GeoLocation" -> GeoLocation
            "PayPal" -> PayPal
            "Bitcoin" -> Bitcoin
            "Zoom" -> Zoom
            "Snapchat" -> Snapchat
            else -> null
        }
    }
}