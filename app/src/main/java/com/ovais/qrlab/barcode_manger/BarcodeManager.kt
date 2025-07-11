package com.ovais.qrlab.barcode_manger

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import androidx.core.graphics.createBitmap
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.ovais.qrlab.logger.QRLogger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber


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

    private fun logInfo(content: String) {
        logger.logInfo(BARCODE_MANAGER_LOG_TAG + content)
    }

    private fun logException(content: String) {
        logger.logException(BARCODE_MANAGER_LOG_TAG + content)
    }
}