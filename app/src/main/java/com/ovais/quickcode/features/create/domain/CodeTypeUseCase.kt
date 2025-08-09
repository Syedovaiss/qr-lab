package com.ovais.quickcode.features.create.domain

import com.ovais.quickcode.R
import com.ovais.quickcode.features.create.data.CodeItem
import com.ovais.quickcode.features.create.data.CodeType
import com.ovais.quickcode.features.create.data.FieldMetaData
import com.ovais.quickcode.utils.BODY
import com.ovais.quickcode.utils.EMAIL
import com.ovais.quickcode.utils.LATITUDE
import com.ovais.quickcode.utils.LONGITUDE
import com.ovais.quickcode.utils.NUMBER
import com.ovais.quickcode.utils.PHONE
import com.ovais.quickcode.utils.StringResourceProvider
import com.ovais.quickcode.utils.TEXT
import com.ovais.quickcode.utils.URL
import com.ovais.quickcode.utils.inputs.InputType
import com.ovais.quickcode.utils.usecase.UseCase

interface CodeTypeUseCase : UseCase<List<CodeItem>>

class DefaultCodeTypeUseCase(
    private val stringResourceProvider: StringResourceProvider
) : CodeTypeUseCase {
    override fun invoke(): List<CodeItem> {
        return mutableListOf(
            CodeItem(
                R.drawable.ic_text,
                CodeType.Text,
                R.string.text,
                listOf(
                    FieldMetaData(
                        name = TEXT,
                        hint = stringResourceProvider.get(R.string.enter_your_text),
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
                        hint = stringResourceProvider.get(R.string.sample_url),
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
                        hint = stringResourceProvider.get(R.string.hint_username),
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
                        hint = stringResourceProvider.get(R.string.hint_username),
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
                        hint = stringResourceProvider.get(R.string.phone_number),
                        inputType = InputType.Number
                    ),
                    FieldMetaData(
                        name = TEXT,
                        hint = stringResourceProvider.get(R.string.your_message),
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
                        hint = stringResourceProvider.get(R.string.hint_username),
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
                        hint = stringResourceProvider.get(R.string.sample_email),
                        inputType = InputType.Email
                    ),
                    FieldMetaData(
                        name = BODY,
                        hint = stringResourceProvider.get(R.string.your_message),
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
                        hint = stringResourceProvider.get(R.string.search_here),
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
                        hint = stringResourceProvider.get(R.string.hint_username),
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
                        hint = stringResourceProvider.get(R.string.hint_username),
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
                        hint = stringResourceProvider.get(R.string.phone_number),
                        inputType = InputType.Number
                    ),
                    FieldMetaData(
                        name = TEXT,
                        hint = stringResourceProvider.get(R.string.your_message),
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
                        hint = stringResourceProvider.get(R.string.phone_number),
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
                        hint = stringResourceProvider.get(R.string.hint_username),
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
                        hint = stringResourceProvider.get(R.string.latitude),
                        inputType = InputType.Location
                    ),
                    FieldMetaData(
                        name = LONGITUDE,
                        hint = stringResourceProvider.get(R.string.longitude),
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
                        hint = stringResourceProvider.get(R.string.hint_username),
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
                        hint = stringResourceProvider.get(R.string.crypto_hint),
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
                        hint = stringResourceProvider.get(R.string.meeting_url),
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
                        hint = stringResourceProvider.get(R.string.hint_username),
                        inputType = InputType.Text
                    )
                )
            )
        )
    }
}