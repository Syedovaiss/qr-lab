package com.ovais.qrlab.features.create.domain

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.zxing.BarcodeFormat
import com.ovais.qrlab.barcode_manger.BarcodeConfig
import com.ovais.qrlab.barcode_manger.BarcodeManager
import com.ovais.qrlab.features.create.data.CodeFormats
import com.ovais.qrlab.features.create.data.CodeResult
import com.ovais.qrlab.features.create.data.CodeType
import com.ovais.qrlab.features.create.data.CreateCodeRepository
import com.ovais.qrlab.features.create.data.toValidFormat
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.math.log

class DefaultCreateCodeRepository(
    private val barcodeManager: BarcodeManager,
    private val dispatcherIO: CoroutineDispatcher
) : CreateCodeRepository {
    override suspend fun createBarcode(
        selectedContentMap: MutableMap<String, String>,
        format: CodeFormats?,
        type: CodeType
    ): CodeResult {
        return withContext(dispatcherIO) {
            format?.let { barcodeFormat ->
                val content = getContentBasedOnType(selectedContentMap, type)
                val config = BarcodeConfig(
                    width = 600,
                    height = 300,
                    barcodeFormat = barcodeFormat.toValidFormat()
                )
                barcodeManager.generateBarcode(content, config)?.let {
                    CodeResult.Success(it)
                } ?: CodeResult.Failure("Failed to create barcode!")

            } ?: CodeResult.Failure("Invalid code format!")
        }
    }

    override suspend fun createQRCode(
        selectedContentMap: MutableMap<String, String>,
        type: CodeType,
        colors: Pair<Color, Color>,
        logo: Bitmap?
    ): CodeResult {
        return withContext(dispatcherIO) {
            val content = getContentBasedOnType(selectedContentMap, type)
            val config = BarcodeConfig(
                width = 1024,
                height = 1024,
                barcodeFormat = BarcodeFormat.QR_CODE
            )
            barcodeManager.generateQRCode(
                content = content,
                size = config.width,
                backgroundColor = colors.first.toArgb(),
                foregroundColor = colors.second.toArgb(),
                logoBitmap = logo
            )?.let {
                CodeResult.Success(it)
            } ?: CodeResult.Failure("Failed to create barcode!")

        }
    }

    private fun getContentBasedOnType(
        selectedContentMap: MutableMap<String, String>,
        type: CodeType
    ): String {
        return when (type) {
            is CodeType.Text -> {
                selectedContentMap[com.ovais.qrlab.utils.TEXT].orEmpty()
            }

            is CodeType.Website -> {
                selectedContentMap[com.ovais.qrlab.utils.URL].orEmpty()
            }

            is CodeType.Instagram -> {
                val username = selectedContentMap[com.ovais.qrlab.utils.TEXT].orEmpty()
                "https://instagram.com/$username"
            }

            is CodeType.Facebook -> {
                val username = selectedContentMap[com.ovais.qrlab.utils.TEXT].orEmpty()
                "https://facebook.com/$username"
            }

            is CodeType.WhatsApp -> {
                val phone = selectedContentMap[com.ovais.qrlab.utils.NUMBER].orEmpty()
                "https://wa.me/$phone"
            }

            is CodeType.YouTube -> {
                val id = selectedContentMap[com.ovais.qrlab.utils.TEXT].orEmpty()
                if (id.startsWith("http")) id else "https://youtube.com/$id"
            }

            is CodeType.Email -> {
                val email = selectedContentMap[com.ovais.qrlab.utils.EMAIL].orEmpty()
                val body = selectedContentMap[com.ovais.qrlab.utils.BODY]
                if (body.isNullOrBlank()) {
                    "mailto:$email"
                } else {
                    "mailto:$email?body=${body}"
                }
            }

            is CodeType.Search -> {
                val query = selectedContentMap[com.ovais.qrlab.utils.TEXT].orEmpty()
                "https://www.google.com/search?q=${query}"
            }

            is CodeType.Threads -> {
                val username = selectedContentMap[com.ovais.qrlab.utils.TEXT].orEmpty()
                "https://threads.net/@$username"
            }

            is CodeType.Discord -> {
                val invite = selectedContentMap[com.ovais.qrlab.utils.TEXT].orEmpty()
                "https://discord.gg/$invite"
            }

            is CodeType.SMS -> {
                val phone = selectedContentMap[com.ovais.qrlab.utils.PHONE].orEmpty()
                val message = selectedContentMap[com.ovais.qrlab.utils.TEXT]
                if (message.isNullOrBlank()) {
                    "sms:$phone"
                } else {
                    "sms:$phone?body=${message}"
                }
            }

            is CodeType.Phone -> {
                val phone = selectedContentMap[com.ovais.qrlab.utils.NUMBER].orEmpty()
                "tel:$phone"
            }

            is CodeType.LinkedIn -> {
                val username = selectedContentMap[com.ovais.qrlab.utils.TEXT].orEmpty()
                "https://linkedin.com/in/$username"
            }

            is CodeType.WiFi -> {
                val wifi = selectedContentMap[com.ovais.qrlab.utils.TEXT].orEmpty()
                wifi // You may want to format this as per WiFi QR code standard
            }

            is CodeType.GeoLocation -> {
                val lat = selectedContentMap[com.ovais.qrlab.utils.LATITUDE].orEmpty()
                val lng = selectedContentMap[com.ovais.qrlab.utils.LONGITUDE].orEmpty()
                "geo:$lat,$lng"
            }

            is CodeType.PayPal -> {
                val link = selectedContentMap[com.ovais.qrlab.utils.TEXT].orEmpty()
                "https://paypal.me/$link"
            }

            is CodeType.Bitcoin -> {
                val address = selectedContentMap[com.ovais.qrlab.utils.TEXT].orEmpty()
                "bitcoin:$address"
            }

            is CodeType.Zoom -> {
                val link = selectedContentMap[com.ovais.qrlab.utils.TEXT].orEmpty()
                link
            }

            is CodeType.Snapchat -> {
                val username = selectedContentMap[com.ovais.qrlab.utils.TEXT].orEmpty()
                "https://snapchat.com/add/$username"
            }
        }
    }


}