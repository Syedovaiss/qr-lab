package com.ovais.qrlab.barcode_manger

import android.graphics.Color
import com.google.zxing.BarcodeFormat


private interface Config {
    val width: Int
    val height: Int
    val textSize: Float?
    val textColor: Int
}

data class BarcodeConfig(
    override val width: Int,
    override val height: Int,
    override val textSize: Float? = null,
    override val textColor: Int = Color.BLACK,
    val barcodeFormat: BarcodeFormat = BarcodeFormat.CODE_128
) : Config
