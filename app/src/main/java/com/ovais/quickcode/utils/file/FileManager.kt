package com.ovais.quickcode.utils.file

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.content.FileProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File

interface FileManager {
    fun createImageUri(): Uri
    suspend fun uriToBitmap(uri: Uri): Bitmap?
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
}