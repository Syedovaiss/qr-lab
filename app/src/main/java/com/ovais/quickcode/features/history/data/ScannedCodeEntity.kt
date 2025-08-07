package com.ovais.quickcode.features.history.data

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ovais.quickcode.utils.KeyValue

@Entity(tableName = "code_scanned")
data class ScannedCodeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val content: List<KeyValue>,
    val bitmap: Bitmap?,
    val scannedAt: String
) 