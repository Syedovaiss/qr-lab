package com.ovais.quickcode.features.create.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ovais.quickcode.features.create.data.CodeFormats
import com.ovais.quickcode.utils.components.CustomDropdown

@Composable
fun CodeFormatDropDown(
    items: MutableList<CodeFormats>,
    selectedType: CodeFormats?,
    label: Int,
    onTypeSelected: (CodeFormats) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        CustomDropdown(
            items = items,
            selectedItem = selectedType,
            onItemSelected = onTypeSelected,
            label = stringResource(label),
            itemToString = { it.title }
        )
    }
}