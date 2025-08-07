package com.ovais.quickcode.features.history.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.ovais.quickcode.R
import com.ovais.quickcode.features.create.data.CodeType

@Composable
fun CodeTypeIcon(
    codeType: CodeType,
    modifier: Modifier = Modifier
) {
    val resourceId = when (codeType) {
        is CodeType.Text -> R.drawable.ic_text
        is CodeType.Website -> R.drawable.ic_website
        is CodeType.Instagram -> R.drawable.ic_instagram
        is CodeType.Facebook -> R.drawable.ic_facebook
        is CodeType.WhatsApp -> R.drawable.ic_whatsapp
        is CodeType.YouTube -> R.drawable.ic_youtube
        is CodeType.Email -> R.drawable.ic_email
        is CodeType.Search -> R.drawable.ic_google
        is CodeType.Threads -> R.drawable.ic_threads
        is CodeType.Discord -> R.drawable.ic_discord
        is CodeType.SMS -> R.drawable.ic_sms
        is CodeType.Phone -> R.drawable.ic_phone
        is CodeType.LinkedIn -> R.drawable.ic_linkedin
        is CodeType.GeoLocation -> R.drawable.ic_location
        is CodeType.PayPal -> R.drawable.ic_paypal
        is CodeType.Bitcoin -> R.drawable.ic_bitcoin
        is CodeType.Zoom -> R.drawable.ic_zoom
        is CodeType.Snapchat -> R.drawable.ic_snapchat
    }

    Image(
        painter = painterResource(resourceId),
        contentDescription = null,
        modifier = modifier
    )
}