package com.ovais.qrlab.barcode_manger

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Typeface
import android.util.TypedValue
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale
import androidx.core.graphics.set
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.ovais.qrlab.logger.QRLogger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.EnumMap


interface BarcodeManager {
    suspend fun generateBarcode(
        content: String,
        config: BarcodeConfig
    ): Bitmap?

    suspend fun generateBarcode(
        content: String,
        text: String,
        config: BarcodeConfig
    ): Bitmap?

    suspend fun generateBitMatrixForQR(content: String, size: Int): BitMatrix
    suspend fun applyColorPattern(
        bitMatrix: BitMatrix,
        size: Int,
        foregroundColor: Int,
        backgroundColor: Int,
        pattern: PatternType = PatternType.SQUARE
    ): Bitmap

    suspend fun addLogoToCenter(
        qrBitmap: Bitmap,
        logoBitmap: Bitmap,
        logoSizeRatio: Float = 0.2f
    ): Bitmap

    suspend fun generateQRCode(
        content: String,
        size: Int = 800,
        foregroundColor: Int = Color.BLACK,
        backgroundColor: Int = Color.WHITE,
        pattern: PatternType = PatternType.SQUARE,
        logoBitmap: Bitmap? = null,
        logoSizeRatio: Float = 0.2f
    ): Bitmap?
}

class DefaultBarcodeManager(
    private val dispatcherDefault: CoroutineDispatcher,
    private val logger: QRLogger
) : BarcodeManager {

    private companion object Companion {
        private const val BARCODE_MANAGER_LOG_TAG = "Barcode Manager Logs:"
    }

    override suspend fun generateBarcode(
        content: String,
        config: BarcodeConfig
    ): Bitmap? {
        return try {
            if (config.barcodeFormat == BarcodeFormat.QR_CODE) {
                throw BarcodeMethodException()
            }
            logInfo("Generating Barcode for:$content")
            withContext(dispatcherDefault) {
                val bitMatrix: BitMatrix = MultiFormatWriter().encode(
                    content,
                    config.barcodeFormat,
                    config.width,
                    config.height
                )
                logInfo("Created bit-matrix for barcode")
                val bitmap = BarcodeEncoder().createBitmap(bitMatrix)
                logInfo("Bitmap created for:$content")
                bitmap
            }
        } catch (e: Exception) {
            Timber.e(e)
            logException("Failed to generate barcode because: ${e.printStackTrace()}")
            null
        }
    }

    override suspend fun generateBarcode(
        content: String,
        text: String,
        config: BarcodeConfig
    ): Bitmap? {
        return try {
            if (config.barcodeFormat == BarcodeFormat.QR_CODE) {
                throw BarcodeMethodException()
            }
            logInfo("Generating Barcode for:$content")
            withContext(dispatcherDefault) {
                val bitMatrix =
                    MultiFormatWriter().encode(
                        content,
                        config.barcodeFormat,
                        config.width,
                        config.height
                    )
                logInfo("Created bit-matrix for barcode")
                val barcodeBitmap = BarcodeEncoder().createBitmap(bitMatrix)
                logInfo("Bitmap created for:$content")
                logInfo("Applying text on it")
                // Prepare paint for text
                val paint = Paint().apply {
                    color = config.textColor
                    this.textSize = config.textSize ?: textSize
                    textAlign = Paint.Align.CENTER
                    isAntiAlias = true
                    typeface = Typeface.DEFAULT_BOLD
                }
                val textHeight = (paint.descent() - paint.ascent()).toInt()
                // padding between text and barcode
                val totalHeight = barcodeBitmap.height + textHeight + 20
                // Create new bitmap with space for text
                val combinedBitmap = createBitmap(config.width, totalHeight)
                val canvas = Canvas(combinedBitmap)
                canvas.drawColor(Color.WHITE)
                // Draw text at the top center
                canvas.drawText(content, (config.width / 2).toFloat(), -paint.ascent() + 10, paint)
                // Draw barcode below text
                canvas.drawBitmap(barcodeBitmap, 0f, textHeight + 20f, null)
                logInfo("Added Text on Barcode")
                combinedBitmap
            }
        } catch (e: Exception) {
            Timber.e(e)
            logException("Failed to generate barcode because: ${e.printStackTrace()}")
            null
        }
    }

    override suspend fun generateBitMatrixForQR(
        content: String,
        size: Int
    ): BitMatrix {
        val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java).apply {
            put(EncodeHintType.CHARACTER_SET, "UTF-8")
            put(EncodeHintType.MARGIN, 1)
            put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H)
        }

        return MultiFormatWriter().encode(
            content,
            BarcodeFormat.QR_CODE,
            size,
            size,
            hints
        )
    }

    override suspend fun applyColorPattern(
        bitMatrix: BitMatrix,
        size: Int,
        foregroundColor: Int,
        backgroundColor: Int,
        pattern: PatternType
    ): Bitmap {
        val bitmap = createBitmap(size, size)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
        }

        val dotSize = 1f // adjust if scaling down for spacing

        for (y in 0 until size) {
            for (x in 0 until size) {
                val drawPixel = bitMatrix.get(x, y)
                paint.color = if (drawPixel) foregroundColor else backgroundColor

                if (!drawPixel) {
                    bitmap[x, y] = backgroundColor
                    continue
                }

                when (pattern) {
                    PatternType.SQUARE -> bitmap[x, y] = foregroundColor

                    PatternType.CIRCLE -> canvas.drawCircle(
                        x.toFloat(),
                        y.toFloat(),
                        dotSize,
                        paint
                    )

                    PatternType.ROUNDED_RECT -> {
                        val left = x.toFloat() - 0.5f
                        val top = y.toFloat() - 0.5f
                        val right = x.toFloat() + 0.5f
                        val bottom = y.toFloat() + 0.5f
                        canvas.drawRoundRect(RectF(left, top, right, bottom), 0.4f, 0.4f, paint)
                    }

                    PatternType.DIAMOND -> {
                        val path = Path().apply {
                            moveTo(x.toFloat(), y.toFloat() - dotSize)
                            lineTo(x.toFloat() + dotSize, y.toFloat())
                            lineTo(x.toFloat(), y.toFloat() + dotSize)
                            lineTo(x.toFloat() - dotSize, y.toFloat())
                            close()
                        }
                        canvas.drawPath(path, paint)
                    }
                }
            }
        }

        return bitmap
    }

    override suspend fun addLogoToCenter(
        qrBitmap: Bitmap,
        logoBitmap: Bitmap,
        logoSizeRatio: Float
    ): Bitmap {
        val finalBitmap = qrBitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(finalBitmap)

        // Scale the logo to desired size
        val scaledLogo = logoBitmap.scale(
            (qrBitmap.width * logoSizeRatio).toInt(),
            (qrBitmap.height * logoSizeRatio).toInt()
        )

        // Convert 12dp to pixels
        val cornerRadius = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            12f,
            Resources.getSystem().displayMetrics
        )

        // Create rounded logo bitmap
        val roundedLogo = createBitmap(scaledLogo.width, scaledLogo.height)

        val logoCanvas = Canvas(roundedLogo)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        val rect = RectF(0f, 0f, scaledLogo.width.toFloat(), scaledLogo.height.toFloat())
        logoCanvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)

        // Use DST_IN to apply rounded mask
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        logoCanvas.drawBitmap(scaledLogo, 0f, 0f, paint)
        paint.xfermode = null

        // Center the rounded logo on QR
        val left = (qrBitmap.width - roundedLogo.width) / 2f
        val top = (qrBitmap.height - roundedLogo.height) / 2f

        canvas.drawBitmap(roundedLogo, left, top, null)

        return finalBitmap
    }

    override suspend fun generateQRCode(
        content: String,
        size: Int,
        foregroundColor: Int,
        backgroundColor: Int,
        pattern: PatternType,
        logoBitmap: Bitmap?,
        logoSizeRatio: Float
    ): Bitmap? {
        return try {
            val matrix = generateBitMatrixForQR(content, size)
            var qrBitmap =
                applyColorPattern(matrix, size, foregroundColor, backgroundColor, pattern)
            logoBitmap?.let {
                qrBitmap = addLogoToCenter(qrBitmap, logoBitmap, logoSizeRatio)
            }
            qrBitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun logInfo(content: String) {
        logger.logInfo(BARCODE_MANAGER_LOG_TAG + content)
    }

    private fun logException(content: String) {
        logger.logException(BARCODE_MANAGER_LOG_TAG + content)
    }
}

enum class PatternType {
    SQUARE,
    CIRCLE,
    ROUNDED_RECT,
    DIAMOND
}