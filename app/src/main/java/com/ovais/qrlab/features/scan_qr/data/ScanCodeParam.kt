package com.ovais.qrlab.features.scan_qr.data

import android.net.Uri
import androidx.camera.core.ImageProxy

data class ScanCodeParam(
    val imageProxy: ImageProxy? = null,
    val type: ScanCodeParamType? = null,
    val uri: Uri? = null
)

sealed interface ScanCodeParamType {
    data object ImageProxy : ScanCodeParamType
    data object Uri : ScanCodeParamType
}
