package com.ovais.qrlab.features.create.domain

import com.ovais.qrlab.R
import com.ovais.qrlab.features.create.data.CodeItem
import com.ovais.qrlab.features.create.data.CodeType
import com.ovais.qrlab.features.create.data.FieldMetaData
import com.ovais.qrlab.utils.BODY
import com.ovais.qrlab.utils.DATE_END
import com.ovais.qrlab.utils.DATE_START
import com.ovais.qrlab.utils.EMAIL
import com.ovais.qrlab.utils.HINT_CRYPTO
import com.ovais.qrlab.utils.HINT_DATE_END
import com.ovais.qrlab.utils.HINT_DATE_START
import com.ovais.qrlab.utils.HINT_DESCRIPTION
import com.ovais.qrlab.utils.HINT_EMAIL
import com.ovais.qrlab.utils.HINT_ENTER_TEXT
import com.ovais.qrlab.utils.HINT_LATITUDE
import com.ovais.qrlab.utils.HINT_LONGITUDE
import com.ovais.qrlab.utils.HINT_MEETING_URL
import com.ovais.qrlab.utils.HINT_MESSAGE
import com.ovais.qrlab.utils.HINT_NAME
import com.ovais.qrlab.utils.HINT_PASSWORD
import com.ovais.qrlab.utils.HINT_PHONE_WITH_COUNTRY
import com.ovais.qrlab.utils.HINT_SEARCH
import com.ovais.qrlab.utils.HINT_TITLE
import com.ovais.qrlab.utils.HINT_URL
import com.ovais.qrlab.utils.HINT_USERNAME
import com.ovais.qrlab.utils.HINT_WIFI_NAME
import com.ovais.qrlab.utils.LATITUDE
import com.ovais.qrlab.utils.LONGITUDE
import com.ovais.qrlab.utils.NUMBER
import com.ovais.qrlab.utils.PASSWORD
import com.ovais.qrlab.utils.PHONE
import com.ovais.qrlab.utils.TEXT
import com.ovais.qrlab.utils.URL
import com.ovais.qrlab.utils.inputs.InputType
import com.ovais.qrlab.utils.usecase.UseCase

interface CodeTypeUseCase : UseCase<List<CodeItem>>

class DefaultCodeTypeUseCase : CodeTypeUseCase {
    override fun invoke(): List<CodeItem> {
        return mutableListOf(
            CodeItem(
                R.drawable.ic_text,
                CodeType.Text,
                R.string.text,
                listOf(
                    FieldMetaData(
                        name = TEXT,
                        hint = HINT_ENTER_TEXT,
                        inputType = InputType.Text
                    )
                )
            ),
            CodeItem(
                R.drawable.ic_website,
                CodeType.Website,
                R.string.website,
                listOf(
                    FieldMetaData(
                        name = URL,
                        hint = HINT_URL,
                        inputType = InputType.Url
                    )
                )

            ),
            CodeItem(
                R.drawable.ic_instagram,
                CodeType.Instagram,
                R.string.instagram,
                listOf(
                    FieldMetaData(
                        name = TEXT,
                        hint = HINT_USERNAME,
                        inputType = InputType.Text
                    )
                )
            ),
            CodeItem(
                R.drawable.ic_facebook,
                CodeType.Facebook,
                R.string.facebook,
                listOf(
                    FieldMetaData(
                        name = TEXT,
                        hint = HINT_USERNAME,
                        inputType = InputType.Text
                    )
                )
            ),
            CodeItem(
                R.drawable.ic_whatsapp,
                CodeType.WhatsApp,
                R.string.whatsapp,
                listOf(
                    FieldMetaData(
                        name = NUMBER,
                        hint = HINT_PHONE_WITH_COUNTRY,
                        inputType = InputType.Number
                    ),
                    FieldMetaData(
                        name = TEXT,
                        hint = HINT_MESSAGE,
                        inputType = InputType.Text
                    )
                )
            ),
            CodeItem(
                R.drawable.ic_youtube,
                CodeType.YouTube,
                R.string.youtube,
                listOf(
                    FieldMetaData(
                        name = TEXT,
                        hint = HINT_USERNAME,
                        inputType = InputType.Text
                    )
                )
            ),
            CodeItem(
                R.drawable.ic_email,
                CodeType.Email,
                R.string.email,
                listOf(
                    FieldMetaData(
                        name = EMAIL,
                        hint = HINT_EMAIL,
                        inputType = InputType.Email
                    ),
                    FieldMetaData(
                        name = BODY,
                        hint = "Message body...",
                        inputType = InputType.Text
                    ),
                )
            ),
            CodeItem(
                R.drawable.ic_google,
                CodeType.Search,
                R.string.google,
                listOf(
                    FieldMetaData(
                        name = TEXT,
                        hint = HINT_SEARCH,
                        inputType = InputType.Text
                    )
                )
            ),
            CodeItem(
                R.drawable.ic_threads,
                CodeType.Threads,
                R.string.threads,
                listOf(
                    FieldMetaData(
                        name = TEXT,
                        hint = HINT_USERNAME,
                        inputType = InputType.Text
                    )
                )
            ),
            CodeItem(
                R.drawable.ic_discord,
                CodeType.Discord,
                R.string.discord,
                listOf(
                    FieldMetaData(
                        name = TEXT,
                        hint = HINT_USERNAME,
                        inputType = InputType.Text
                    )
                )
            ),
            CodeItem(
                R.drawable.ic_sms,
                CodeType.SMS,
                R.string.sms,
                listOf(
                    FieldMetaData(
                        name = PHONE,
                        hint = HINT_PHONE_WITH_COUNTRY,
                        inputType = InputType.Number
                    ),
                    FieldMetaData(
                        name = TEXT,
                        hint = "Message Body",
                        inputType = InputType.Text
                    ),
                )
            ),
            CodeItem(
                R.drawable.ic_phone,
                CodeType.Phone,
                R.string.phone,
                listOf(
                    FieldMetaData(
                        name = NUMBER,
                        hint = HINT_PHONE_WITH_COUNTRY,
                        inputType = InputType.Phone
                    )
                )
            ),
            CodeItem(
                R.drawable.ic_linkedin,
                CodeType.LinkedIn,
                R.string.linkedin,
                listOf(
                    FieldMetaData(
                        name = TEXT,
                        hint = HINT_USERNAME,
                        inputType = InputType.Text
                    )
                )
            ),
            CodeItem(
                R.drawable.ic_location,
                CodeType.GeoLocation,
                R.string.location,
                listOf(
                    FieldMetaData(
                        name = LATITUDE,
                        hint = HINT_LATITUDE,
                        inputType = InputType.Location
                    ),
                    FieldMetaData(
                        name = LONGITUDE,
                        hint = HINT_LONGITUDE,
                        inputType = InputType.Location
                    )
                )
            ),
            CodeItem(
                R.drawable.ic_paypal,
                CodeType.PayPal,
                R.string.paypal,
                listOf(
                    FieldMetaData(
                        name = TEXT,
                        hint = HINT_USERNAME,
                        inputType = InputType.Text
                    )
                )
            ),
            CodeItem(
                R.drawable.ic_bitcoin,
                CodeType.Bitcoin,
                R.string.bitcoin,
                listOf(
                    FieldMetaData(
                        name = TEXT,
                        hint = HINT_CRYPTO,
                        inputType = InputType.Text
                    )
                )
            ),
            CodeItem(
                R.drawable.ic_zoom,
                CodeType.Zoom,
                R.string.zoom,
                listOf(
                    FieldMetaData(
                        name = TEXT,
                        hint = HINT_MEETING_URL,
                        inputType = InputType.Text
                    )
                )
            ),
            CodeItem(
                R.drawable.ic_snapchat,
                CodeType.Snapchat,
                R.string.snapchat,
                listOf(
                    FieldMetaData(
                        name = TEXT,
                        hint = HINT_USERNAME,
                        inputType = InputType.Text
                    )
                )
            )
        )
    }
}