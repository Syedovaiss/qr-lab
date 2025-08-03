package com.ovais.quickcode.utils.file

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.content.FileProvider
import com.ovais.quickcode.utils.QRFormat
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

interface FileManager {
    fun createImageUri(): Uri
    suspend fun uriToBitmap(uri: Uri): Bitmap?
    suspend fun saveImage(
        bitmap: Bitmap,
        uri: Uri,
        format: QRFormat = QRFormat.PNG
    ): FileResult

    suspend fun getContentUri(bitmap: Bitmap): Uri
}

class DefaultFileManager(
    private val context: Context,
    private val dispatcherIO: CoroutineDispatcher
) : FileManager {
    override fun createImageUri(): Uri {
        val file = File.createTempFile("IMG_", ".jpg", context.cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    }

    override suspend fun uriToBitmap(uri: Uri): Bitmap? {
        return withContext(dispatcherIO) {
            try {
                val stream = context.contentResolver.openInputStream(uri)
                BitmapFactory.decodeStream(stream).also { stream?.close() }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    override suspend fun saveImage(bitmap: Bitmap, uri: Uri, format: QRFormat): FileResult {
        return withContext(dispatcherIO) {
            try {
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    bitmap.compress(getCompressionFormat(format), 100, outputStream)
                }
                FileResult.Success
            } catch (e: Exception) {
                e.printStackTrace()
                FileResult.Failure(e.message.toString())
            }
        }
    }

    private fun getCompressionFormat(format: QRFormat): Bitmap.CompressFormat {
        return if (format == QRFormat.PNG) Bitmap.CompressFormat.PNG else Bitmap.CompressFormat.JPEG
    }

    override suspend fun getContentUri(bitmap: Bitmap): Uri {
        return withContext(dispatcherIO) {
            val cachePath = File(context.cacheDir, "images")
            cachePath.mkdirs()
            val file = File(cachePath, "shared_barcode.png")
            FileOutputStream(file).use { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            }
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )
        }
    }
}