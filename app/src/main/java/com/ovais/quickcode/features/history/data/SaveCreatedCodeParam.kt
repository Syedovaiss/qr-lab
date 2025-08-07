package com.ovais.quickcode.features.history.data

import android.graphics.Bitmap
import com.ovais.quickcode.features.create.data.CodeFormats
import com.ovais.quickcode.features.create.data.CodeType
import com.ovais.quickcode.utils.KeyValue

data class SaveCreatedCodeParam(
    val content: List<KeyValue>,
    val codeType: CodeType,
    val format: CodeFormats,
    val foregroundColor: String,
    val backgroundColor: String,
    val width: Int,
    val height: Int,
    val logo: Bitmap?,
    val createdAt: String
)
