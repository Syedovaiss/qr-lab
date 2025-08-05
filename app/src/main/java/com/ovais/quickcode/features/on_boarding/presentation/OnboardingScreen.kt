package com.ovais.quickcode.features.on_boarding.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ovais.quickcode.R
import com.ovais.quickcode.utils.components.BodyText
import com.ovais.quickcode.utils.components.PrimaryButton
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardingScreen(
    viewModel: OnBoardingViewModel = koinViewModel(),
    onFinish: () -> Unit
) {
    val onboardingPages by viewModel.onBoardingItems.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
        val scope = rememberCoroutineScope()
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            OnboardingPageContent(onboardingPages[page])
        }

        Spacer(Modifier.height(24.dp))

        DotsIndicator(
            totalDots = onboardingPages.size,
            selectedIndex = pagerState.currentPage
        )

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BodyText(
                stringResource(R.string.skip),
                modifier = Modifier.clickable(
                    remember { MutableInteractionSource() },
                    rememberRipple()
                ) {
                    viewModel.onOnBoardingFinish {
                        onFinish()
                    }
                }
            )
            val resId =
                if (pagerState.currentPage == onboardingPages.lastIndex) R.string.get_started else R.string.next
            PrimaryButton(
                modifier = Modifier
                    .defaultMinSize(minWidth = 100.dp)
                    .wrapContentHeight(),
                title = resId,
                onClick = {
                    if (pagerState.currentPage == onboardingPages.lastIndex) {
                        viewModel.onOnBoardingFinish {
                            onFinish()
                        }
                    } else {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                }
            )
        }
    }
}