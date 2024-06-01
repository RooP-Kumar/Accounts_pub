package com.zen.accounts.ui.screens.common

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.zen.accounts.R
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.disabled_color
import com.zen.accounts.ui.theme.editTextCursorColor
import com.zen.accounts.ui.theme.enabled_color
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.halfGeneralPadding
import com.zen.accounts.ui.theme.normalPadding
import com.zen.accounts.ui.theme.normalTextSize
import com.zen.accounts.ui.theme.onBackground
import com.zen.accounts.ui.theme.onSurface
import com.zen.accounts.ui.theme.placeholder
import com.zen.accounts.ui.theme.surface

data object CustomKeyboardOptions {
    val default = KeyboardOptions.Default
    val textEditor = KeyboardOptions.Default.copy(
        capitalization = KeyboardCapitalization.Sentences,
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next
    )
    val lastTextEditor = KeyboardOptions.Default.copy(
        capitalization = KeyboardCapitalization.Sentences,
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Done
    )
    val numberEditor = KeyboardOptions.Default.copy(
        keyboardType = KeyboardType.Phone,
        imeAction = ImeAction.Next
    )
    val emailEditor = KeyboardOptions.Default.copy(
        capitalization = KeyboardCapitalization.None,
        keyboardType = KeyboardType.Email,
        imeAction = ImeAction.Next
    )
    val passwordEditor = KeyboardOptions.Default.copy(
        capitalization = KeyboardCapitalization.None,
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Done
    )
}
@Composable
fun GeneralEditText(
    text: String,
    modifier: Modifier,
    onValueChange : (String) -> Unit = {},
    placeholderText: String = "Enter Text",
    singleLine: Boolean = true,
    enable: Boolean = true,
    keyboardOptions: KeyboardOptions = CustomKeyboardOptions.default,
    clickableFun: (() -> Unit)? = null,
    showClickEffect : Boolean = true,
    @DrawableRes trailingIcon: Int? = null,
    trailingIconClick: (() -> Unit)? = null
) {
    val mutablePasswordVisible = rememberSaveable {
        mutableStateOf(keyboardOptions.keyboardType != KeyboardType.Password)
    }
    val passwordVisible = remember { derivedStateOf { mutablePasswordVisible.value } }
    val customSelectionColor = TextSelectionColors(
        handleColor = onBackground,
        backgroundColor = onBackground.copy(alpha = 0.4f)
    )
    CompositionLocalProvider(LocalTextSelectionColors provides customSelectionColor) {
        BasicTextField(
            value = text,
            onValueChange = {
                onValueChange(it)
            },
            modifier = modifier
                .padding(horizontal = generalPadding, vertical = halfGeneralPadding)
                .clip(RoundedCornerShape(generalPadding))
                .background(surface)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = if (showClickEffect) LocalIndication.current else null
                ) {
                    if (clickableFun != null) {
                        clickableFun()
                    }
                }
                .padding(generalPadding),
            singleLine = singleLine,
            cursorBrush = Brush.linearGradient(listOf(editTextCursorColor, editTextCursorColor)),
            textStyle = TextStyle.Default.copy(
                color = onSurface,
                fontFamily = FontFamily(Font(R.font.montserrat)),
                fontSize = normalTextSize
            ),
            enabled = enable,
            keyboardOptions = keyboardOptions,
            visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation()
        ) {
            it()
            Row(
                Modifier
            ) {
                if (text.isEmpty()) {
                    Text(
                        text = placeholderText,
                        style = TextStyle.Default.copy(
                            color = placeholder,
                            fontFamily = FontFamily(Font(R.font.montserrat)),
                            fontSize = normalTextSize
                        )
                    )
                }

                if (keyboardOptions.keyboardType == KeyboardType.Password) {
                    val passIcon =
                        if (passwordVisible.value) R.drawable.ic_eye else R.drawable.ic_eye_off
                    Column(
                        Modifier.weight(1f)
                    ) {
                        Icon(
                            painter = painterResource(passIcon),
                            contentDescription = "eye_icon",
                            modifier = Modifier
                                .align(Alignment.End)
                                .clickable {
                                    mutablePasswordVisible.value = !mutablePasswordVisible.value
                                },
                            tint = onSurface
                        )
                    }
                }

                if (trailingIcon != null) {
                    Column(
                        Modifier.weight(1f)
                    ) {
                        Icon(
                            painter = painterResource(trailingIcon),
                            contentDescription = "eye_icon",
                            modifier = Modifier
                                .align(Alignment.End)
                                .clickable(
                                    interactionSource = remember {
                                        MutableInteractionSource()
                                    },
                                    indication = null
                                ) {
                                    if (trailingIconClick != null) {
                                        trailingIconClick()
                                    }
                                },
                            tint = onSurface
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun GeneralButton(
    text: String,
    modifier: Modifier? = null,
    containerColor: Color = enabled_color,
    disabledContainerColor: Color = disabled_color,
    enable: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            ?: Modifier
                .fillMaxWidth()
                .padding(horizontal = generalPadding, vertical = halfGeneralPadding),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            disabledContainerColor = disabledContainerColor
        ),
        enabled = enable,
        shape = RoundedCornerShape(generalPadding),
        onClick = { onClick() }
    ) {
        Text(
            text = text,
            style = Typography.bodyMedium.copy(color = Color.White),
            modifier = Modifier.padding(vertical = normalPadding)
        )
    }
}


@Composable
fun GeneralDropDown(
    modifier: Modifier,
    value : MutableState<BackupPlan>,
    showDropDown : MutableState<Boolean>,
    enable : Boolean = false,
    valueList : List<BackupPlan>,
    placeholderText: String = "Select Expense Type",
    onClick: () -> Unit = {},
    onItemClick : (BackupPlan) -> Unit
) {
    val localDensity = LocalDensity.current
    val dropDownHeight : MutableState<Dp> = remember { mutableStateOf(0.dp) }
    val dropDownRowWidth = rememberSaveable(saver = dpSaver) { mutableStateOf(0.dp) }
    val dropDownIcon = rememberSaveable { mutableIntStateOf(R.drawable.ic_drop_down) }
    val text = remember {
        derivedStateOf { value.value.label }
    }

    if(!enable){
        value.value = BackupPlan.Off
    }

    Row(
        modifier = modifier
    ) {
        GeneralEditText(
            text = text.value,
            modifier = Modifier
                .onGloballyPositioned {
                    dropDownRowWidth.value =
                        with(localDensity) { it.size.width.toDp() }
                }
            ,
            enable = false,
            placeholderText = placeholderText,
            clickableFun = {
                if(enable) {
                    showDropDown.value = !showDropDown.value
                }
                onClick()
            },
            trailingIcon = dropDownIcon.intValue,
            trailingIconClick = {
                if(enable)
                    showDropDown.value = !showDropDown.value
            }
        )

        DropdownMenu(
            expanded = showDropDown.value,
            onDismissRequest = {
                showDropDown.value = false
            },
            modifier = Modifier.size(width = dropDownRowWidth.value - generalPadding.times(2), height = dropDownHeight.value),
            offset = DpOffset(dropDownRowWidth.value / 2 - (dropDownRowWidth.value - generalPadding.times(2))/2, (-4).dp)
        ) {
            for (i in valueList.indices) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = valueList[i].label,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    onClick = {
                        onItemClick(valueList[i])
                        value.value = valueList[i]
                        showDropDown.value = false
                    },
                    modifier = Modifier
                        .onGloballyPositioned {
                            dropDownHeight.value = with(localDensity) {it.size.height.toDp()} * if (valueList.size < 6) valueList.size else 5
                        }
                )
            }
        }
    }
}

@Composable
fun GeneralSnackBar(
    visible : MutableState<Boolean>,
    text : String,
    modifier: Modifier
) {
    AnimatedVisibility(
        visible = visible.value,
        enter = slideInVertically(initialOffsetY = { -300 }) + fadeIn(),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { -300 })
    ) {
        Snackbar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(generalPadding)
                .then(modifier),
            containerColor = surface,
            contentColor = onSurface,
            shape = RoundedCornerShape(generalPadding)
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(generalPadding),
                style = Typography.bodyMedium.copy(color = onSurface)
            )
        }
    }
}

