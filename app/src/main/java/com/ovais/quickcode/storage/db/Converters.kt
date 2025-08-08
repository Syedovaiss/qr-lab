package com.ovais.quickcode.storage.db

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.ovais.quickcode.features.create.data.CodeFormats
import com.ovais.quickcode.features.create.data.CodeType
import com.ovais.quickcode.utils.KeyValue
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream

class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromCodeType(codeType: CodeType): String {
        return codeType.name
    }

    @TypeConverter
    fun toCodeType(name: String): CodeType? {
        return CodeType.fromName(name)
    }

    @TypeConverter
    fun fromCodeFormat(format: CodeFormats): String {
        return format.title
    }

    @TypeConverter
    fun toCodeFormat(title: String): CodeFormats? {
        return CodeFormats.fromTitle(title)
    }


    @TypeConverter
    fun fromBitmap(bitmap: Bitmap?): ByteArray? {
        if (bitmap == null) return null
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(bytes: ByteArray?): Bitmap? {
        return bytes?.let {
            BitmapFactory.decodeByteArray(it, 0, it.size)
        }
    }


    @TypeConverter
    fun fromKeyValues(value: List<KeyValue>?): String? {
        return value?.let { json.encodeToString(it) }
    }

    @TypeConverter
    fun toKeyValues(value: String?): List<KeyValue>? {
        return value?.let { json.decodeFromString(it) }
    }
} 