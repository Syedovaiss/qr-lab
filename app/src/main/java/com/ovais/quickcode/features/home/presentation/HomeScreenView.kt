package com.ovais.quickcode.features.home.presentation

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ovais.quickcode.R
import com.ovais.quickcode.core.ui.theme.HomeCardOne
import com.ovais.quickcode.core.ui.theme.appBackground
import com.ovais.quickcode.features.home.data.CardItemType
import com.ovais.quickcode.features.home.data.HomeCardItem
import com.ovais.quickcode.features.home.data.UserInfo
import com.ovais.quickcode.utils.components.AvatarView
import com.ovais.quickcode.utils.components.BodyText
import com.ovais.quickcode.utils.components.HeadingText
import com.ovais.quickcode.utils.components.SubtitleText
import com.ovais.quickcode.utils.openURL
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel
import java.util.Calendar

@Composable
fun HomeScreenView(
    scaffoldPadding: PaddingValues = PaddingValues(),
    snackBarHostState: SnackbarHostState,
    viewModel: HomeViewModel = koinViewModel(),
    onClick: (HomeAction) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.nextDestination.collectLatest { intent ->
            onClick(intent)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.termsAndConditionsIUrl.collectLatest {
            context.openURL(it)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.errorMessage.collectLatest {
            snackBarHostState.showSnackbar(it)
        }
    }
    when (uiState) {
        is HomeUiState.Loading -> {
            HomeScreenLoadingView()
        }

        is HomeUiState.Loaded -> {
            HomeScreen(
                scaffoldPadding,
                (uiState as HomeUiState.Loaded).uiData,
                onCardClick = { type ->
                    viewModel.onAction(type)
                },
                onTermsAndConditionsClicked = {
                    viewModel.onTermsAndConditions()
                },
                onUserClick = {
                    viewModel.onUserClick(context)
                }
            )
        }
    }
}


@Composable
fun TopView(
    userInfo: UserInfo,
    onUserClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .padding(16.dp)
            .clickable(
                remember { MutableInteractionSource() },
                LocalIndication.current
            ) { onUserClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        AvatarView(
            imageUrl = userInfo.avatar,
            initials = userInfo.initials
        )
        SubtitleText(
            userInfo.name.split(" ").take(2).joinToString("\n"),
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

@Composable
fun HomeScreen(
    scaffoldPadding: PaddingValues,
    uiData: HomeUiData,
    onCardClick: (CardItemType) -> Unit,
    onTermsAndConditionsClicked: () -> Unit,
    onUserClick: () -> Unit
) {
    val termsAndConditionsInteractionSource = remember { MutableInteractionSource() }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(scaffoldPadding)
            .background(appBackground)
    ) {

        TopView(uiData.userInfo, onUserClick)
        HeadingText(
            text = stringResource(R.string.home_screen_title)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(uiData.cardItem) { item ->
                HomeScreenCard(
                    gradientColors = item.gradientColors,
                    text = stringResource(item.title),
                    icon = item.iconRes
                ) {
                    onCardClick(item.type)
                }
            }
        }

        Spacer(Modifier.weight(1f))

        BodyText(
            text = stringResource(R.string.trademark, Calendar.getInstance()[Calendar.YEAR]),
            color = Color.Gray,
            textAlign = TextAlign.Center,
            paddingValues = PaddingValues(
                vertical = 0.dp
            ),
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        SubtitleText(
            text = stringResource(R.string.terms_and_conditions),
            textColor = Color.Gray,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            paddingValues = PaddingValues(
                vertical = 8.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    termsAndConditionsInteractionSource,
                    rememberRipple()
                ) {
                    onTermsAndConditionsClicked()
                }
        )

    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        scaffoldPadding = PaddingValues(16.dp),
        HomeUiData(
            userInfo = UserInfo(
                name = "Syed Ovais Akhtar",
                initials = "SOA"
            ),
            cardItem = listOf(
                HomeCardItem(
                    R.drawable.ic_create_qr,
                    R.string.create_qr,
                    CardItemType.Create,
                    HomeCardOne
                )
            )
        ),
        onCardClick = {},
        onTermsAndConditionsClicked = {},
        onUserClick = {}
    )
}