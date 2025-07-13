package com.ovais.qrlab.features.create.domain

import com.ovais.qrlab.R
import com.ovais.qrlab.features.create.data.CodeItem
import com.ovais.qrlab.features.create.data.CodeType
import com.ovais.qrlab.features.create.data.FieldMetaData
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
                        name = "text",
                        hint = "Enter your text",
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
                        name = "url",
                        hint = "https://www.example.com",
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
                        name = "text",
                        hint = "@username",
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
                        name = "text",
                        hint = "@username",
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
                        name = "number",
                        hint = "phone number with country code",
                        inputType = InputType.Number
                    ),
                    FieldMetaData(
                        name = "text",
                        hint = "Your Message....",
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
                        name = "text",
                        hint = "@username",
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
                        name = "email",
                        hint = "someone@example.com",
                        inputType = InputType.Email
                    ),
                    FieldMetaData(
                        name = "body",
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
                        name = "text",
                        hint = "search here",
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
                        name = "text",
                        hint = "@username",
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
                        name = "text",
                        hint = "@username",
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
                        name = "phone",
                        hint = "Phone number with country code",
                        inputType = InputType.Number
                    ),
                    FieldMetaData(
                        name = "text",
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
                        name = "number",
                        hint = "Phone number with country code",
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
                        name = "text",
                        hint = "@username",
                        inputType = InputType.Text
                    )
                )
            ),
            CodeItem(
                R.drawable.ic_wifi,
                CodeType.WiFi,
                R.string.wifi,
                listOf(
                    FieldMetaData(
                        name = "text",
                        hint = "Wifi Name",
                        inputType = InputType.Text
                    ),
                    FieldMetaData(
                        name = "password",
                        hint = "Password or empty if public...",
                        inputType = InputType.Password
                    ),
                )
            ),
            CodeItem(
                R.drawable.ic_contact,
                CodeType.Contact,
                R.string.contact,
                listOf(
                    FieldMetaData(
                        name = "text",
                        hint = "Name",
                        inputType = InputType.Text
                    )
                )
            ),
            CodeItem(
                R.drawable.ic_calendar,
                CodeType.Calendar,
                R.string.calendar,
                listOf(
                    FieldMetaData(
                        name = "text",
                        hint = "Title",
                        inputType = InputType.Text
                    ),
                    FieldMetaData(
                        name = "body",
                        hint = "Description",
                        inputType = InputType.Text
                    ),
                    FieldMetaData(
                        name = "date",
                        hint = "Start Date Time",
                        inputType = InputType.DateTime
                    ),
                    FieldMetaData(
                        name = "date",
                        hint = "End Date Time",
                        inputType = InputType.DateTime
                    ),
                )
            ),
            CodeItem(
                R.drawable.ic_location,
                CodeType.GeoLocation,
                R.string.location,
                listOf(
                    FieldMetaData(
                        name = "latitude",
                        hint = "Latitude",
                        inputType = InputType.Location
                    ),
                    FieldMetaData(
                        name = "longitude",
                        hint = "Longitude",
                        inputType = InputType.Location
                    )
                )
            ),
            CodeItem(
                R.drawable.ic_skype,
                CodeType.Skype,
                R.string.skype,
                listOf(
                    FieldMetaData(
                        name = "text",
                        hint = "@username",
                        inputType = InputType.Text
                    )
                )
            ),
            CodeItem(
                R.drawable.ic_paypal,
                CodeType.PayPal,
                R.string.paypal,
                listOf(
                    FieldMetaData(
                        name = "text",
                        hint = "@username",
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
                        name = "text",
                        hint = "Please fill in the crypto address",
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
                        name = "text",
                        hint = "Meeting URL...",
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
                        name = "text",
                        hint = "@username",
                        inputType = InputType.Text
                    )
                )
            )
        )
    }
}