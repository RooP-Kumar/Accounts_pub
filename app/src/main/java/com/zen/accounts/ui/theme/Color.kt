package com.zen.accounts.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// surface colors
val lightSurface = Color(0xFFF6F6F6)
val darkSurface = Color(0xFF1B1B1B)

// Shadow Color
val shadowColor @Composable get() =
    if (isSystemInDarkTheme()) Color.White else Color.Black

// text colors
val placeholder @Composable get() =
    if(isSystemInDarkTheme()) Color(0xFF979797) else Color(0xFFA2A2A2)

val background @Composable get() =
    if(isSystemInDarkTheme()) Color.Black else Color.White

val surface @Composable get() =
    if (isSystemInDarkTheme()) darkSurface else lightSurface

val onBackground @Composable get() =
    if (isSystemInDarkTheme()) Color.White else Color.Black

val editTextCursorColor @Composable get() =
    if (isSystemInDarkTheme()) Color.White else Color.Black

val onSurface @Composable get() =
    if (isSystemInDarkTheme()) Color(0xFFFFFFFF) else Color(0xFF000000)