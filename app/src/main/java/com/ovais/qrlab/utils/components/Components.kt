package com.ovais.qrlab.utils.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ovais.qrlab.core.ui.font.Poppins


@Composable
fun HeadingText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 28.sp,
    fontFamily: FontFamily = Poppins,
    fontWeight: FontWeight = FontWeight.Bold,
    textAlign: TextAlign = TextAlign.Start,
    paddingValues: PaddingValues = PaddingValues(vertical = 40.dp, horizontal = 16.dp)
) {
    Text(
        text = text,
        modifier = modifier
            .fillMaxWidth()
            .padding(paddingValues),
        fontSize = fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign,
        fontFamily = fontFamily
    )
}

@Composable
fun SubtitleText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 20.sp,
    fontFamily: FontFamily = Poppins,
    fontWeight: FontWeight = FontWeight.SemiBold,
    textAlign: TextAlign = TextAlign.Start,
    paddingValues: PaddingValues = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
) {
    Text(
        text = text,
        modifier = modifier
            .fillMaxWidth()
            .padding(paddingValues),
        fontSize = fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign,
        fontFamily = fontFamily
    )
}


@Composable
fun BodyText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 16.sp,
    fontFamily: FontFamily = Poppins,
    fontWeight: FontWeight = FontWeight.Medium,
    textAlign: TextAlign = TextAlign.Start,
    paddingValues: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
) {
    Text(
        text = text,
        modifier = modifier
            .fillMaxWidth()
            .padding(paddingValues),
        fontSize = fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign,
        fontFamily = fontFamily
    )
}