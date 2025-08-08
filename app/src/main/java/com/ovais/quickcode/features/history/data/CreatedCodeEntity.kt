package com.ovais.quickcode.features.history.data

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ovais.quickcode.features.create.data.CodeFormats
import com.ovais.quickcode.features.create.data.CodeType
import com.ovais.quickcode.utils.KeyValue

@Entity(tableName = "code_created")
data class CreatedCodeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "code_id")
    val id: Long = 0,
    @ColumnInfo("content_json")
    val content: List<KeyValue>,
    @ColumnInfo("code_type")
    val codeType: CodeType,
    @ColumnInfo("code_format")
    val format: CodeFormats,
    @ColumnInfo("code_color")
    val foregroundColor: String,
    @ColumnInfo("code_background")
    val backgroundColor: String,
    val width: Int,
    val height: Int,
    @ColumnInfo("code_image")
    val image: Bitmap? = null,
    @ColumnInfo("created_at")
    val createdAt: String
)