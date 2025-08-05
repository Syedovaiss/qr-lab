package com.ovais.quickcode.features.code_details.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ovais.quickcode.R
import com.ovais.quickcode.core.ui.theme.ColorPrimary
import com.ovais.quickcode.core.ui.theme.ErrorColor
import com.ovais.quickcode.utils.components.BodyText
import com.ovais.quickcode.utils.components.ComposableLottieAnimation
import com.ovais.quickcode.utils.components.SubtitleText

@Composable
fun SaveBarcodeDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            title = {
                SubtitleText(
                    text = stringResource(R.string.save_barcode),
                    fontSize = 18.sp
                )
            },
            text = {
                BodyText(
                    text = stringResource(R.string.save_barcode_message),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    BodyText(
                        text = stringResource(R.string.save),
                        color = ColorPrimary
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    BodyText(
                        text = stringResource(R.string.cancel),
                        color = Color.Gray
                    )
                }
            }
        )
    }
}

@Composable
fun SuccessDialog(
    show: Boolean,
    onDismiss: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            title = {
                SubtitleText(
                    text = stringResource(R.string.success),
                    fontSize = 18.sp,
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ComposableLottieAnimation(
                        Modifier
                            .size(100.dp)
                            .padding(vertical = 8.dp),
                        resId = R.raw.success
                    )
                    BodyText(
                        text = stringResource(R.string.image_saved_successfully),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    BodyText(
                        text = stringResource(R.string.ok),
                        color = ColorPrimary
                    )
                }
            }
        )
    }
}

@Composable
fun ErrorDialog(
    show: Boolean,
    onDismiss: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            title = {
                SubtitleText(
                    text = stringResource(R.string.error),
                    fontSize = 18.sp,
                    textColor = ErrorColor
                )
            },
            text = {
                BodyText(
                    text = stringResource(R.string.failed_to_save_image),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    BodyText(
                        text = stringResource(R.string.ok),
                        color = ColorPrimary
                    )
                }
            }
        )
    }
}