package com.ovais.quickcode.utils.file

import android.content.ContentValues
import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.ovais.quickcode.features.history.data.CreatedCodeEntity
import com.ovais.quickcode.features.history.data.ScannedCodeEntity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

interface FileExportHelper {
    suspend fun exportScannedToCsv(
        data: List<ScannedCodeEntity>,
        fileName: String
    )

    suspend fun exportToPDF(
        createdData: List<CreatedCodeEntity>,
        scannedData: List<ScannedCodeEntity>,
        fileName: String
    ): Boolean

    suspend fun exportCreatedToCsv(
        data: List<CreatedCodeEntity>,
        fileName: String
    )
}

class DefaultFileExportHelper(
    private val context: Context
) : FileExportHelper {

    override suspend fun exportScannedToCsv(
        data: List<ScannedCodeEntity>,
        fileName: String
    ) {
        val csvContent = buildString {
            append("Content,ScannedAt\n")
            data.forEach {
                append("${it.content},${it.scannedAt}\n")
            }
        }
        saveToDownloads("$fileName.csv", "text/csv", csvContent.toByteArray())
    }

    override suspend fun exportCreatedToCsv(
        data: List<CreatedCodeEntity>,
        fileName: String
    ) {
        val csvContent = buildString {
            append("Content,CodeType,Format,CreatedAt\n")
            data.forEach {
                val contentStr = it.content.joinToString { kv -> "${kv.key}:${kv.value}" }
                append("$$contentStr,${it.codeType},${it.format},${it.createdAt}\n")
            }
        }
        saveToDownloads("$fileName.csv", "text/csv", csvContent.toByteArray())
    }

    override suspend fun exportToPDF(
        createdData: List<CreatedCodeEntity>,
        scannedData: List<ScannedCodeEntity>,
        fileName: String
    ): Boolean {
        val pdfDocument = PdfDocument()
        val paint = android.graphics.Paint()
        val titlePaint = android.graphics.Paint().apply {
            textSize = 18f
            isFakeBoldText = true
        }

        var pageNumber = 1
        var yPosition = 50

        fun startNewPage(): PdfDocument.Page {
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, pageNumber).create()
            val page = pdfDocument.startPage(pageInfo)
            yPosition = 50
            return page
        }

        var page = startNewPage()
        var canvas = page.canvas

        // Title
        canvas.drawText("QR/Barcode Data Export", 40f, yPosition.toFloat(), titlePaint)
        yPosition += 30

        // Scanned Codes
        canvas.drawText("Scanned Codes:", 40f, yPosition.toFloat(), titlePaint)
        yPosition += 20
        scannedData.forEach { scanned ->
            if (yPosition > 700) {  // reserve space for images
                pdfDocument.finishPage(page)
                pageNumber++
                page = startNewPage()
                canvas = page.canvas
            }

            // Draw content and timestamp (without ID)
            canvas.drawText(
                "Content: ${scanned.content} | Scanned At: ${scanned.scannedAt}",
                40f,
                yPosition.toFloat(),
                paint
            )
            yPosition += 20

            // Draw bitmap if available
            scanned.bitmap?.let { bmp ->
                val maxWidth = 200f
                val aspectRatio = bmp.height.toFloat() / bmp.width
                val bmpHeight = maxWidth * aspectRatio
                if (yPosition + bmpHeight > 800) {
                    pdfDocument.finishPage(page)
                    pageNumber++
                    page = startNewPage()
                    canvas = page.canvas
                }
                canvas.drawBitmap(
                    bmp,
                    null,
                    android.graphics.RectF(
                        40f,
                        yPosition.toFloat(),
                        40f + maxWidth,
                        yPosition + bmpHeight
                    ),
                    paint
                )
                yPosition += bmpHeight.toInt() + 20
            }
        }

        yPosition += 30
        canvas.drawText("Created Codes:", 40f, yPosition.toFloat(), titlePaint)
        yPosition += 20
        createdData.forEach { created ->
            if (yPosition > 700) {
                pdfDocument.finishPage(page)
                pageNumber++
                page = startNewPage()
                canvas = page.canvas
            }

            // Summarize key-values and show type + createdAt timestamp (no ID)
            val contentSummary = created.content.joinToString(", ") { "${it.key}: ${it.value}" }
            canvas.drawText(
                "Type: ${created.codeType} | Created At: ${created.createdAt} | $contentSummary",
                40f,
                yPosition.toFloat(),
                paint
            )
            yPosition += 20

            // Draw bitmap if available
            created.image?.let { bmp ->
                val maxWidth = 200f
                val aspectRatio = bmp.height.toFloat() / bmp.width
                val bmpHeight = maxWidth * aspectRatio
                if (yPosition + bmpHeight > 800) {
                    pdfDocument.finishPage(page)
                    pageNumber++
                    page = startNewPage()
                    canvas = page.canvas
                }
                canvas.drawBitmap(
                    bmp,
                    null,
                    android.graphics.RectF(
                        40f,
                        yPosition.toFloat(),
                        40f + maxWidth,
                        yPosition + bmpHeight
                    ),
                    paint
                )
                yPosition += bmpHeight.toInt() + 20
            }
        }

        pdfDocument.finishPage(page)

        val byteStream = ByteArrayOutputStream()
        pdfDocument.writeTo(byteStream)
        pdfDocument.close()

        saveToDownloads("$fileName.pdf", "application/pdf", byteStream.toByteArray())

        return true
    }

    private fun saveToDownloads(fileName: String, mimeType: String, data: ByteArray) {
        val resolver = context.contentResolver

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+ (Scoped Storage)
            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, mimeType)
                put(
                    MediaStore.Downloads.RELATIVE_PATH,
                    Environment.DIRECTORY_DOWNLOADS + "/Exports"
                )
            }

            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            uri?.let {
                resolver.openOutputStream(it)?.use { output ->
                    output.write(data)
                }
            }
        } else {
            // Android 9 and below
            val downloadsDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val exportDir = File(downloadsDir, "Exports")
            if (!exportDir.exists()) exportDir.mkdirs()

            val file = File(exportDir, fileName)
            try {
                FileOutputStream(file).use { output ->
                    output.write(data)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}