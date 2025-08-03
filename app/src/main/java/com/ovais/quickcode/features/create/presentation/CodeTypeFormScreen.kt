package com.ovais.quickcode.features.create.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ovais.quickcode.R
import com.ovais.quickcode.features.create.data.CodeItem
import com.ovais.quickcode.features.create.data.CodeType
import com.ovais.quickcode.utils.components.SubtitleText


@Composable
fun CodeTypeFormScreen(
    codeItems: List<CodeItem>,
    onCreateCode: (MutableMap<String, String>, type: CodeType) -> Unit
) {
    var selectedCodeItem by remember { mutableStateOf<CodeItem?>(null) }
    val formState = remember { mutableStateMapOf<String, String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        SubtitleText(
            text = stringResource(R.string.platform)
        )
        CodeTypeSelector(
            codeItems = codeItems,
            selectedType = selectedCodeItem?.type,
            onSelected = { item ->
                selectedCodeItem = item
                formState.clear()
            }
        )

        selectedCodeItem?.let { item ->
            Spacer(Modifier.height(16.dp))
            SubtitleText(
                text = stringResource(R.string.enter_details, stringResource(item.title))
            )
            DynamicQRInputForm(
                fields = item.fieldMetaData,
                inputState = formState,
                onValueChange = { key, value -> formState[key] = value }
            )
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = {
                    onCreateCode(formState, item.type)
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            ) {
                Text("Create Code")
            }
        }
    }
}
