package com.ovais.quickcode.barcode_manger

private const val INVALID_METHOD_FOR_QR = "Use other methods for QR Code Generation"

class BarcodeMethodException : Exception() {
    override val message: String?
        get() = INVALID_METHOD_FOR_QR
}