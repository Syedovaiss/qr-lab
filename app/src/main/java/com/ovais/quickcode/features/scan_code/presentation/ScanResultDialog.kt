package com.ovais.quickcode.features.scan_code.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ovais.quickcode.R
import com.ovais.quickcode.utils.components.BodyText
import com.ovais.quickcode.utils.components.ComposableLottieAnimation
import com.ovais.quickcode.utils.components.SubtitleText

@Composable
fun ScanResultDialog(
    resultText: String,
    onDismiss: () -> Unit,
    copy: () -> Unit,
    modifier: Modifier = Modifier
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.close))
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier,
        text = {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Lottie animation
                ComposableLottieAnimation(
                    modifier = Modifier.size(120.dp),
                    resId = R.raw.success
                )

                Spacer(modifier = Modifier.height(16.dp))

                SubtitleText(
                    text = stringResource(R.string.scan_successful),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Result Text
                BodyText(
                    text = resultText,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Copy Button
                OutlinedButton(
                    onClick = {
                        copy()
                    }
                ) {
                    Icon(painter = painterResource(R.drawable.ic_copy), contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    BodyText(stringResource(R.string.copy))
                }
            }
        }
    )
}