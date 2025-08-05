package com.ovais.quickcode.features.on_boarding.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ovais.quickcode.features.on_boarding.data.OnboardingPage
import com.ovais.quickcode.utils.components.BodyText
import com.ovais.quickcode.utils.components.ComposableLottieAnimation
import com.ovais.quickcode.utils.components.HeadingText

@Composable
fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ComposableLottieAnimation(
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth(),
            page.lottieRes
        )

        Spacer(modifier = Modifier.height(32.dp))

        HeadingText(
            text = stringResource(page.title),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        BodyText(
            text = stringResource(page.description),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
