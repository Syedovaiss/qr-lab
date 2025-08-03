package com.ovais.quickcode.utils.components

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.ovais.quickcode.R
import com.ovais.quickcode.core.ui.font.Poppins
import com.ovais.quickcode.core.ui.theme.CardElevated
import com.ovais.quickcode.core.ui.theme.InitialsBgColors
import com.ovais.quickcode.core.ui.theme.colorsForColorPicker
import com.ovais.quickcode.utils.file.FileManager
import kotlinx.coroutines.launch


@Composable
fun HeadingText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 28.sp,
    fontFamily: FontFamily = Poppins,
    fontWeight: FontWeight = FontWeight.Bold,
    textAlign: TextAlign = TextAlign.Start,
    color: Color = Color.Black,
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
        fontFamily = fontFamily,
        color = color
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
    color: Color = Color.Black,
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
        fontFamily = fontFamily,
        color = color
    )
}

@Composable
fun GradientIconCard(
    modifier: Modifier = Modifier,
    gradientColors: List<Color>,
    iconContent: @Composable () -> Unit,
    text: String,
    textColor: Color = Color.White,
    elevation: Dp = 16.dp,
    shadowColor: Color = CardElevated,
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        modifier = modifier
            .drawBehind {
                drawRect(
                    color = shadowColor,
                    topLeft = this.center.copy(x = 0f, y = 0f),
                    size = this.size,
                    alpha = 0.6f
                )
            }
            .clip(RoundedCornerShape(16.dp)) // optional if you're not applying shape to Surface itself
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current
            ) { onClick() },
        tonalElevation = elevation,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = elevation,
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .background(Brush.verticalGradient(gradientColors))
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
            color = Color.White // explicitly white background
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .widthIn(min = 300.dp, max = 360.dp)
                    .verticalScroll(rememberScrollState()) // enable scroll if needed
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))

                val rowCount = (colors.size + 4) / 5 // 5 columns per row
                val estimatedHeight = rowCount * 52.dp // 40dp size + 12dp spacing

                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .heightIn(max = estimatedHeight.coerceAtMost(300.dp)) // limit to avoid overgrowth
                        .fillMaxWidth()
                ) {
                    items(colors.size) { index ->
                        val color = colors[index]
                        val isSelected = color == selectedColor
                        val interactionSource = remember { MutableInteractionSource() }

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = if (isSelected) Color.Black else Color.LightGray,
                                    shape = CircleShape
                                )
                                .clip(CircleShape)
                                .background(Color.White)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = rememberRipple(bounded = true, radius = 24.dp)
                                ) {
                                    onColorSelected(color)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(color)
                            )
                        }
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

@Composable
fun AvatarView(
    imageUrl: String? = null,
    initials: String,
    size: Dp = 64.dp
) {

    val strokeColor = Color.LightGray

    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(if (imageUrl != null) Color.Transparent else InitialsBgColors.random())
            .border(1.dp, strokeColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Avatar Image",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                text = initials,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun ShimmerLine(
    width: Dp,
    height: Dp = 14.dp,
    cornerRadius: Dp = 6.dp
) {
    val transition = rememberInfiniteTransition()
    val shimmerTranslate by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            Color.LightGray.copy(alpha = 0.6f),
            Color.White.copy(alpha = 0.3f),
            Color.LightGray.copy(alpha = 0.6f)
        ),
        start = Offset.Zero,
        end = Offset(x = shimmerTranslate, y = shimmerTranslate)
    )
    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(cornerRadius))
            .background(shimmerBrush)
    )
}

@Composable
fun ShimmerCircle(
    size: Dp = 48.dp
) {
    val transition = rememberInfiniteTransition()
    val shimmerTranslate by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            Color.LightGray.copy(alpha = 0.6f),
            Color.White.copy(alpha = 0.3f),
            Color.LightGray.copy(alpha = 0.6f)
        ),
        start = Offset.Zero,
        end = Offset(x = shimmerTranslate, y = shimmerTranslate)
    )
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(shimmerBrush)
    )
}

@Composable
fun ShimmerCardPlaceholder(
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition()
    val shimmerTranslate by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            Color.LightGray.copy(alpha = 0.6f),
            Color.White.copy(alpha = 0.3f),
            Color.LightGray.copy(alpha = 0.6f)
        ),
        start = Offset.Zero,
        end = Offset(x = shimmerTranslate, y = shimmerTranslate)
    )


    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(shimmerBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(shimmerBrush)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .height(14.dp)
                    .width(60.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(shimmerBrush)
            )
        }
    }
}

@Composable
fun AppSwitch(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    lineWidth: Dp = 40.dp,
    lineHeight: Dp = 3.dp,
    circleSize: Dp = 24.dp,
    trackColorChecked: Color = Color(0xFF7c71b1),       // Purple
    trackColorUnchecked: Color = Color(0xFFf1f1f1),     // Light Gray
    thumbColorChecked: Color = Color(0xFFc0aefb),       // Dark Purple
    thumbColorUnchecked: Color = Color(0xFFf1f1f1),     // Gray
    disabledTrackColor: Color = Color(0xFFE0E0E0),
    disabledThumbColor: Color = Color(0xFFBDBDBD)
) {
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) lineWidth - circleSize else 0.dp,
        animationSpec = tween(durationMillis = 200),
        label = "ThumbOffset"
    )

    val trackColor = when {
        !enabled -> disabledTrackColor
        checked -> trackColorChecked
        else -> trackColorUnchecked
    }

    val thumbColor = when {
        !enabled -> disabledThumbColor
        checked -> thumbColorChecked
        else -> thumbColorUnchecked
    }

    Box(
        modifier = modifier
            .width(lineWidth)
            .height(circleSize),
        contentAlignment = Alignment.CenterStart
    ) {
        // Track line
        Box(
            modifier = Modifier
                .height(lineHeight)
                .fillMaxWidth()
                .background(trackColor)
                .align(Alignment.Center)
        )

        // Thumb (circle)
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(circleSize)
                .clip(CircleShape)
                .background(thumbColor)
                .clickable(
                    enabled = enabled,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = LocalIndication.current
                ) {
                    onCheckedChange(!checked)
                }
        )
    }
}

@Composable
fun BackIcon(callback: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    Image(
        painter = painterResource(R.drawable.ic_back),
        contentDescription = null,
        modifier = Modifier
            .padding(PaddingValues(16.dp))
            .size(24.dp)
            .clickable(
                interactionSource,
                rememberRipple()
            ) { callback() }
    )
}