package com.ovais.qrlab.features.create.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ovais.qrlab.R
import com.ovais.qrlab.features.create.data.CodeFormats
import com.ovais.qrlab.features.create.data.CodeItem
import com.ovais.qrlab.features.create.data.CodeType
import com.ovais.qrlab.features.create.data.FieldMetaData
import com.ovais.qrlab.utils.components.BodyText
import com.ovais.qrlab.utils.components.CustomDropdown
import com.ovais.qrlab.utils.components.HeadingText
import com.ovais.qrlab.utils.components.SubtitleText
import com.ovais.qrlab.utils.inputs.InputType
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateQRView(
    scaffoldPaddingValues: PaddingValues,
    viewModel: CreateCodeViewModel = koinViewModel()
) {
    val scrollState = rememberScrollState()
    val codeItems by viewModel.codeItems.collectAsStateWithLifecycle()
    val codeFormats by viewModel.codeFormats.collectAsStateWithLifecycle()
    var selectedType by remember { mutableStateOf<CodeFormats?>(null) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(scaffoldPaddingValues)
            .verticalScroll(scrollState)
    ) {
        HeadingText(
            stringResource(R.string.create_new),
            paddingValues = PaddingValues(16.dp)
        )
        SubtitleText(
            stringResource(R.string.code_format),
            paddingValues = PaddingValues(16.dp)
        )
        CodeFormatDropDown(codeFormats, selectedType) {
            selectedType = it
        }
        CodeTypeFormScreen(
            codeItems,
            onCreateCode = { selectedValues, type->
                viewModel.createCode(selectedValues, selectedType,type)
            }
        )
    }
}

@Composable
fun CodeFormatDropDown(
    items: MutableList<CodeFormats>,
    selectedType: CodeFormats?,
    onTypeSelected: (CodeFormats) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        CustomDropdown(
            items = items,
            selectedItem = selectedType,
            onItemSelected = onTypeSelected,
            label = "Select Barcode Type",
            itemToString = { it.title }
        )
    }
}

@Composable
fun CodeTypeFormScreen(
    codeItems: List<CodeItem>,
    onCreateCode: (MutableMap<String, String>,type: CodeType) -> Unit
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
                    onCreateCode(formState,item.type)
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            ) {
                Text("Create Code")
            }
        }
    }
}

@Composable
fun CodeTypeSelector(
    codeItems: List<CodeItem>,
    selectedType: CodeType?,
    onSelected: (CodeItem) -> Unit
) {

    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(220.dp)
    ) {
        items(codeItems) { item ->
            val interactionSource = remember { MutableInteractionSource() }
            val isSelected = item.type == selectedType

            Card(
                modifier = Modifier
                    .size(width = 140.dp, height = 100.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = LocalIndication.current
                    ) { onSelected(item) },
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) Color(0xFFE3F2FD) else MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    Image(
                        painter = painterResource(id = item.icon),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(Modifier.height(6.dp))
                    BodyText(
                        text = stringResource(item.title),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun DynamicQRInputForm(
    fields: List<FieldMetaData>,
    inputState: Map<String, String>,
    onValueChange: (String, String) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        fields.forEach { field ->
            OutlinedTextField(
                value = inputState[field.name] ?: "",
                onValueChange = { onValueChange(field.name, it) },
                label = { Text(field.hint) },
                keyboardOptions = when (field.inputType) {
                    InputType.Text -> KeyboardOptions.Default
                    InputType.Number -> KeyboardOptions(keyboardType = KeyboardType.Number)
                    InputType.Url -> KeyboardOptions(keyboardType = KeyboardType.Uri)
                    InputType.Email -> KeyboardOptions(keyboardType = KeyboardType.Email)
                    InputType.Phone -> KeyboardOptions(keyboardType = KeyboardType.Phone)
                    InputType.Password -> KeyboardOptions(keyboardType = KeyboardType.Password)
                    InputType.DateTime -> KeyboardOptions.Default // optional: add date picker
                    InputType.Location -> KeyboardOptions(keyboardType = KeyboardType.Number)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            )
        }
    }
}