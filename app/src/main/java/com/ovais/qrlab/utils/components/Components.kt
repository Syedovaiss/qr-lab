package com.ovais.qrlab.utils.components

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.ovais.qrlab.R
import com.ovais.qrlab.core.ui.font.Poppins
import com.ovais.qrlab.core.ui.theme.colorsForColorPicker
import com.ovais.qrlab.utils.file.FileManager
import kotlinx.coroutines.launch


@Composable
fun HeadingText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 28.sp,
    fontFamily: FontFamily = Poppins,
    fontWeight: FontWeight = FontWeight.Bold,
    textAlign: TextAlign = TextAlign.Start,
    paddingValues: PaddingValues = PaddingValues(vertical = 40.dp, horizontal = 16.dp)
) {
    Text(
        text = text,
        modifier = modifier
            .fillMaxWidth()
            .padding(paddingValues),
        fontSize = fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign,
        fontFamily = fontFamily
    )
}

@Composable
fun SubtitleText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 20.sp,
    fontFamily: FontFamily = Poppins,
    fontWeight: FontWeight = FontWeight.SemiBold,
    textAlign: TextAlign = TextAlign.Start,
    paddingValues: PaddingValues = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
    texColor: Color = Color.Black
) {
    Text(
        text = text,
        modifier = modifier
            .fillMaxWidth()
            .padding(paddingValues),
        fontSize = fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign,
        fontFamily = fontFamily,
        color = texColor
    )
}


@Composable
fun BodyText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 16.sp,
    fontFamily: FontFamily = Poppins,
    fontWeight: FontWeight = FontWeight.Medium,
    textAlign: TextAlign = TextAlign.Start,
    paddingValues: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
) {
    Text(
        text = text,
        modifier = modifier
            .fillMaxWidth()
            .padding(paddingValues),
        fontSize = fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign,
        fontFamily = fontFamily
    )
}

@Composable
fun GradientIconCard(
    modifier: Modifier = Modifier,
    gradientColors: List<Color>,
    iconContent: @Composable () -> Unit,
    text: String,
    textColor: Color = Color.White,
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(gradientColors)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current
            ) { onClick() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            iconContent()
            Spacer(modifier = Modifier.height(8.dp))
            SubtitleText(
                text,
                texColor = textColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> CustomDropdown(
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    itemToString: (T) -> String = { it.toString() }
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedItem?.let { itemToString(it) } ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(itemToString(item)) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ComposablePreview() {
    GradientIconCard(
        gradientColors = listOf(Color.Red, Color.Green),
        modifier = Modifier
            .padding(100.dp)
            .width(150.dp)
            .height(150.dp),
        iconContent = {
            Image(
                painter = painterResource(R.drawable.ic_create_qr),
                contentDescription = null
            )
        },
        text = "Create QR",
        textColor = Color.White,
        onClick = {}
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColorPickerDialog(
    title: String = "Select Color",
    colors: List<Color> = colorsForColorPicker,
    selectedColor: Color = Color.Black,
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            tonalElevation = 8.dp,
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .widthIn(min = 300.dp, max = 360.dp)
            ) {
                Text(title, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .heightIn(max = 240.dp)
                        .fillMaxWidth()
                ) {
                    items(colors.size) { index ->
                        val color = colors[index]
                        val isSelected = color == selectedColor

                        val interactionSource = remember { MutableInteractionSource() }

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = if (isSelected) Color.Black else Color.LightGray,
                                    shape = CircleShape
                                )
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = rememberRipple(bounded = true, radius = 24.dp)
                                ) {
                                    onColorSelected(color)
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ImagePicker(
    fileManager: FileManager,
    onImagePicked: (Bitmap) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            coroutineScope.launch {
                val bitmap = fileManager.uriToBitmap(it)
                bitmap?.let(onImagePicked)
            }
        }
    }
    // Camera capture
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && imageUri != null) {
            coroutineScope.launch {
                val bitmap = fileManager.uriToBitmap(imageUri!!)
                bitmap?.let(onImagePicked)
            }
        }
    }
    Column {
        val boxModifier = Modifier
            .weight(1f)
            .padding(horizontal = 8.dp)
            .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
            .padding(24.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            val cameraInteractionSource = remember { MutableInteractionSource() }
            val galleryInteractionSource = remember { MutableInteractionSource() }
            Box(
                modifier = boxModifier.then(
                    Modifier.clickable(
                        cameraInteractionSource,
                        LocalIndication.current
                    ) {
                        val uri = fileManager.createImageUri()
                        imageUri = uri
                        cameraLauncher.launch(uri)
                    }),
                contentAlignment = Alignment.Center
            ) {

                Image(
                    painter = painterResource(id = R.drawable.ic_camera_add),
                    contentDescription = "Camera",
                    modifier = Modifier
                        .size(36.dp)
                )
            }

            Box(
                modifier = boxModifier.then(
                    Modifier.clickable(
                        galleryInteractionSource,
                        LocalIndication.current
                    ) {
                        galleryLauncher.launch("image/*")
                    }),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_gallery_add),
                    contentDescription = "Gallery",
                    modifier = Modifier
                        .size(36.dp)
                )
            }
        }
    }
}


@Composable
fun PermissionRationaleDialog(
    @StringRes title: Int,
    @StringRes message: Int,
    @StringRes confirmButtonText: Int,
    onDismiss: () -> Unit,
    onConfirmButtonClicked: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(title)) },
        text = { Text(stringResource(message)) },
        confirmButton = {
            TextButton(onClick = onConfirmButtonClicked) {
                Text(stringResource(confirmButtonText))
            }
        }
    )
}

@Composable
fun SizeInputRow(
    defaultWidth: String = "600",
    defaultHeight: String = "600",
    onWidthChange: (String) -> Unit,
    onHeightChange: (String) -> Unit
) {
    var width by remember { mutableStateOf(defaultWidth) }
    var height by remember { mutableStateOf(defaultHeight) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = width,
            onValueChange = {
                width = it
                onWidthChange(it)
            },
            label = { Text(stringResource(R.string.width)) },
            modifier = Modifier
                .weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = height,
            onValueChange = {
                height = it
                onHeightChange(it)
            },
            label = { Text(stringResource(R.string.height)) },
            modifier = Modifier
                .weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@Composable
fun <T> RadioSelectionDialog(
    title: String,
    options: List<T>,
    selectedOption: T?,
    optionLabel: @Composable (T) -> String,
    onOptionSelected: (T) -> Unit,
    onDismissRequest: () -> Unit
) {
    var tempSelectedOption by remember { mutableStateOf(selectedOption) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
        },
        text = {
            Column {
                options.forEach { option ->
                    val interactionSource = remember { MutableInteractionSource() }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = rememberRipple()
                            ) {
                                tempSelectedOption = option
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = option == tempSelectedOption,
                            onClick = { tempSelectedOption = option }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = optionLabel(option))
                    }
                }
            }
        },
        confirmButton = {
            val confirmInteractionSource = remember { MutableInteractionSource() }
            Text(
                text = "Confirm",
                modifier = Modifier
                    .padding(8.dp)
                    .clickable(
                        confirmInteractionSource,
                        rememberRipple()
                    ) {
                        tempSelectedOption?.let { onOptionSelected(it) }
                        onDismissRequest()
                    },
                color = MaterialTheme.colorScheme.primary
            )
        },
        dismissButton = {
            val dismissInteractionSource = remember { MutableInteractionSource() }
            Text(
                text = "Cancel",
                modifier = Modifier
                    .padding(8.dp)
                    .clickable(
                        dismissInteractionSource,
                        rememberRipple()
                    ) { onDismissRequest() },
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    )
}