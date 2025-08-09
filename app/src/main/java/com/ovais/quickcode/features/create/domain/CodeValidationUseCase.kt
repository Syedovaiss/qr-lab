package com.ovais.quickcode.features.create.domain

import com.ovais.quickcode.R
import com.ovais.quickcode.features.create.data.CodeType
import com.ovais.quickcode.features.create.data.CodeValidationParams
import com.ovais.quickcode.utils.EMAIL
import com.ovais.quickcode.utils.LATITUDE
import com.ovais.quickcode.utils.LONGITUDE
import com.ovais.quickcode.utils.NUMBER
import com.ovais.quickcode.utils.PHONE
import com.ovais.quickcode.utils.StringResourceProvider
import com.ovais.quickcode.utils.TEXT
import com.ovais.quickcode.utils.URL
import com.ovais.quickcode.utils.ValidationResult
import com.ovais.quickcode.utils.usecase.ParameterizedUseCase

interface CodeValidationUseCase :
    ParameterizedUseCase<CodeValidationParams, ValidationResult>

class DefaultCodeValidationUseCase(
    private val stringResourceProvider: StringResourceProvider
) : CodeValidationUseCase {

    override fun invoke(param: CodeValidationParams): ValidationResult = when (param.type) {
        is CodeType.Text,
        is CodeType.Instagram,
        is CodeType.Facebook,
        is CodeType.YouTube,
        is CodeType.Search,
        is CodeType.Threads,
        is CodeType.Discord,
        is CodeType.LinkedIn,
        is CodeType.PayPal,
        is CodeType.Bitcoin,
        is CodeType.Zoom,
        is CodeType.Snapchat -> validateTextField(param, TEXT)

        is CodeType.Website -> validateTextField(param, URL, ::validateURL)

        is CodeType.WhatsApp -> validateTextField(param, NUMBER, ::validatePhone)

        is CodeType.Phone -> validateTextField(param, NUMBER, ::validatePhone)

        is CodeType.Email -> {
            val email = param.selectedContentMap[EMAIL]
            val emailResult = validateEmail(email)
            if (emailResult is ValidationResult.InValid) {
                ValidationResult.InValid(stringResourceProvider.get(R.string.enter_valid_email))
            } else ValidationResult.Valid
        }

        is CodeType.SMS -> {
            val phone = param.selectedContentMap[PHONE]
            val message = param.selectedContentMap[TEXT]
            val phoneResult = validatePhone(phone)
            phoneResult as? ValidationResult.InValid ?: validateText(message)
        }

        is CodeType.GeoLocation -> {
            val latitude = param.selectedContentMap[LATITUDE]
            val longitude = param.selectedContentMap[LONGITUDE]
            val latResult = validateText(latitude)
            val lngResult = validateText(longitude)
            when {
                latResult is ValidationResult.InValid -> ValidationResult.InValid(stringResourceProvider.get(R.string.enter_valid_latitude))
                lngResult is ValidationResult.InValid -> ValidationResult.InValid(stringResourceProvider.get(R.string.enter_valid_longitude))
                else -> ValidationResult.Valid
            }
        }
    }

    private fun validateTextField(
        param: CodeValidationParams,
        key: String,
        validator: (String?) -> ValidationResult = this::validateText
    ): ValidationResult {
        val content = param.selectedContentMap[key]
        return validator(content)
    }

    private fun validateText(input: String?): ValidationResult {
        if (input.isNullOrBlank()) {
            return ValidationResult.InValid(stringResourceProvider.get(R.string.enter_valid_value))
        }
        return ValidationResult.Valid
    }

    private fun validateURL(input: String?): ValidationResult {
        return if (input.isNullOrBlank() || isValidUrl(input).not()) {
            ValidationResult.InValid(stringResourceProvider.get(R.string.enter_valid_url))
        } else {
            ValidationResult.Valid
        }
    }

    private fun isValidUrl(url: String): Boolean {
        val urlRegex = Regex(
            "^(https?|ftp)://[\\w.-]+(?:\\.[\\w\\.-]+)+[/#?]?.*\$",
            RegexOption.IGNORE_CASE
        )
        return url.matches(urlRegex)
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex(
            "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        )
        return email.matches(emailRegex)
    }

    private fun validateEmail(input: String?): ValidationResult {
        return if (input.isNullOrBlank() || isValidEmail(input).not()) {
            ValidationResult.InValid(stringResourceProvider.get(R.string.enter_valid_email))
        } else {
            ValidationResult.Valid
        }
    }

    private fun validatePhone(phone: String?): ValidationResult {
        return if (phone.isNullOrBlank()) {
            ValidationResult.InValid(stringResourceProvider.get(R.string.enter_valid_phone))
        } else ValidationResult.Valid
    }

}