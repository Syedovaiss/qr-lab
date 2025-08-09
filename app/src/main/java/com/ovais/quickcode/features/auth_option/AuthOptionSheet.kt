package com.ovais.quickcode.features.auth_option

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ovais.quickcode.R
import com.ovais.quickcode.features.settings.presentation.RowItem
import com.ovais.quickcode.utils.components.AvatarView
import com.ovais.quickcode.utils.components.BodyText
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthBottomSheet(
    sheetState: SheetState,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit,
    name: String,
    initials: String,
    imageUrl: String? = null
) {
    val coroutineScope = rememberCoroutineScope()
    ModalBottomSheet(
        onDismissRequest = {
            coroutineScope.launch {
                sheetState.hide()
            }
        },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AvatarView(
                imageUrl = imageUrl,
                initials = initials,
                size = 80.dp
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Name
            BodyText(
                text = name,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            RowItem(
                label = stringResource(R.string.close_account),
                description = stringResource(R.string.close_account_description),
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_delete_account),
                    null,
                    modifier = Modifier.clickable(
                        remember { MutableInteractionSource() },
                        rememberRipple()
                    ) { onDeleteAccount() }
                )
            }


            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            RowItem(
                label = stringResource(R.string.logout),
                description = stringResource(R.string.logout_description)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_logout),
                    null,
                    modifier = Modifier.clickable(
                        remember { MutableInteractionSource() },
                        rememberRipple()
                    ) { onLogout() }
                )
            }
        }
    }
}