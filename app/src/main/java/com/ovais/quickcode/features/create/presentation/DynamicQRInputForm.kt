package com.ovais.quickcode.features.create.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ovais.quickcode.features.create.data.FieldMetaData
import com.ovais.quickcode.utils.inputs.InputType
import kotlin.collections.forEach


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