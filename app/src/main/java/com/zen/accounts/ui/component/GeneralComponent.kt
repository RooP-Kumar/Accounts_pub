package com.zen.accounts.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.zen.accounts.R
import com.zen.accounts.ui.theme.Purple80
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.editTextCursorColor
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.halfGeneralPadding
import com.zen.accounts.ui.theme.normalPadding
import com.zen.accounts.ui.theme.normalTextSize
import com.zen.accounts.ui.theme.onSurface
import com.zen.accounts.ui.theme.placeholder
import com.zen.accounts.ui.theme.surface

@Composable
fun GeneralEditText(
    text : MutableState<String>,
    modifier: Modifier,
    placeholderText : String = "Enter Text",
    singleLine : Boolean = true,
    enable : Boolean = true,
    keyboardOptions : KeyboardOptions = KeyboardOptions.Default,
    clickableFun : (() -> Unit)? = null,
    @DrawableRes trailingIcon : Int? = null,
    trailingIconClick : (() -> Unit)? = null
) {
    val passwordVisible : MutableState<Boolean> = rememberSaveable {mutableStateOf(true)}
    Box {
        BasicTextField(
            value = text.value,
            onValueChange = {
                text.value = it
            },
            modifier = modifier
                .padding(horizontal = generalPadding, vertical = halfGeneralPadding)
                .clip(RoundedCornerShape(generalPadding))
                .background(surface)
                .clickable{
                    if (clickableFun != null) {
                        clickableFun()
                    }
                }
                .padding(generalPadding)
            ,
            singleLine = singleLine,
            cursorBrush = Brush.linearGradient(listOf(editTextCursorColor, editTextCursorColor)),
            textStyle = TextStyle.Default.copy(
                color = onSurface,
                fontFamily = FontFamily(Font(R.font.montserrat)),
                fontSize = normalTextSize
            ),
            enabled = enable,
            keyboardOptions = keyboardOptions,
            visualTransformation = if(passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation()
        ){
            it()
            Row(
                Modifier.fillMaxWidth()
            ) {
                if(text.value.isEmpty()) {
                    Text(
                        text = placeholderText,
                        style = TextStyle.Default.copy(
                            color = placeholder,
                            fontFamily = FontFamily(Font(R.font.montserrat)),
                            fontSize = normalTextSize
                        )
                    )
                }

                if(keyboardOptions.keyboardType == KeyboardType.Password) {
                    val passIcon = if(passwordVisible.value) R.drawable.ic_eye else R.drawable.ic_eye_off
                    Column(
                        Modifier.weight(1f)
                    ) {
                        Icon(
                            painter = painterResource(passIcon),
                            contentDescription = "eye_icon",
                            modifier = Modifier
                                .align(Alignment.End)
                                .clickable {
                                    passwordVisible.value = !passwordVisible.value
                                }
                        )
                    }
                }

                if(trailingIcon != null) {
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
                                }
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun GeneralButton(
    text : String,
    modifier: Modifier,
    onClick : () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = generalPadding, vertical = halfGeneralPadding),
        colors = ButtonDefaults.buttonColors(
            containerColor = Purple80
        ),
        shape = RoundedCornerShape(generalPadding),
        onClick = { onClick() }
    ) {
        Text(
            text = text,
            style = Typography.bodyMedium.copy(color = onSurface),
            modifier = Modifier.padding(vertical = normalPadding)
        )
    }
}



