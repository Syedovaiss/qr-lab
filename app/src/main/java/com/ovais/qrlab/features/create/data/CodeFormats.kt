package com.ovais.qrlab.features.create.data

import com.google.zxing.BarcodeFormat

sealed interface CodeFormats {
    val title: String

    data object Aztec : CodeFormats {
        override val title = "Aztec"
    }

    data object Codabar : CodeFormats {
        override val title = "Codabar"
    }

    data object Code39 : CodeFormats {
        override val title = "Code 39"
    }

    data object Code93 : CodeFormats {
        override val title = "Code 93"
    }

    data object Code128 : CodeFormats {
        override val title = "Code 128"
    }

    data object DataMatrix : CodeFormats {
        override val title = "Data Matrix"
    }

    data object EAN8 : CodeFormats {
        override val title = "EAN-8"
    }

    data object EAN13 : CodeFormats {
        override val title = "EAN-13"
    }

    data object ITF : CodeFormats {
        override val title = "ITF"
    }

    data object MaxiCode : CodeFormats {
        override val title = "MaxiCode"
    }

    data object PDF417 : CodeFormats {
        override val title = "PDF417"
    }

    data object QRCode : CodeFormats {
        override val title = "QR Code"
    }

    data object RSS14 : CodeFormats {
        override val title = "RSS 14"
    }

    data object RSSExpanded : CodeFormats {
        override val title = "RSS Expanded"
    }

    data object UPCA : CodeFormats {
        override val title = "UPC-A"
    }

    data object UPCE : CodeFormats {
        override val title = "UPC-E"
    }

    data object UPCEANExtension : CodeFormats {
        override val title = "UPC/EAN Extension"
    }

    companion object {
        val formats = mutableListOf(
            Aztec, Codabar, Code39, Code93, Code128, DataMatrix,
            EAN8, EAN13, ITF, MaxiCode, PDF417, QRCode,
            RSS14, RSSExpanded, UPCA, UPCE, UPCEANExtension
        )
    }
}

fun CodeFormats.toValidFormat(): BarcodeFormat = when (this) {
    CodeFormats.Aztec -> BarcodeFormat.AZTEC
    CodeFormats.Codabar -> BarcodeFormat.CODABAR
    CodeFormats.Code39 -> BarcodeFormat.CODE_39
    CodeFormats.Code93 -> BarcodeFormat.CODE_93
    CodeFormats.Code128 -> BarcodeFormat.CODE_128
    CodeFormats.DataMatrix -> BarcodeFormat.DATA_MATRIX
    CodeFormats.EAN8 -> BarcodeFormat.EAN_8
    CodeFormats.EAN13 -> BarcodeFormat.EAN_13
    CodeFormats.ITF -> BarcodeFormat.ITF
    CodeFormats.MaxiCode -> BarcodeFormat.MAXICODE
    CodeFormats.PDF417 -> BarcodeFormat.PDF_417
    CodeFormats.QRCode -> BarcodeFormat.QR_CODE
    CodeFormats.RSS14 -> BarcodeFormat.RSS_14
    CodeFormats.RSSExpanded -> BarcodeFormat.RSS_EXPANDED
    CodeFormats.UPCA -> BarcodeFormat.UPC_A
    CodeFormats.UPCE -> BarcodeFormat.UPC_E
    CodeFormats.UPCEANExtension -> BarcodeFormat.UPC_EAN_EXTENSION
}