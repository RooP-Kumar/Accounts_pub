package com.zen.accounts.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.zen.accounts.states.getAppState

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

// surface colors
val lightSurface = Color(0xFFF6F6F6)
val darkSurface = Color(0xFF0F0F0F)

// Shadow Color
val shadowColor @Composable get() =
    if (getAppState(context = LocalContext.current).darkMode.value ?: isSystemInDarkTheme()) Color.White else Color.Black

// text colors
val placeholder @Composable get() =
    if(getAppState(context = LocalContext.current).darkMode.value ?: isSystemInDarkTheme()) Color(0xFF979797) else Color(0xFFA2A2A2)

val background @Composable get() =
    if(getAppState(context = LocalContext.current).darkMode.value ?: isSystemInDarkTheme()) Color.Black else Color.White

val surface @Composable get() =
    if (getAppState(context = LocalContext.current).darkMode.value ?: isSystemInDarkTheme()) darkSurface else lightSurface

val onBackground @Composable get() =
    if (getAppState(context = LocalContext.current).darkMode.value ?: isSystemInDarkTheme()) Color.White else Color.Black

val editTextCursorColor @Composable get() =
    if (getAppState(context = LocalContext.current).darkMode.value ?: isSystemInDarkTheme()) Color.White else Color.Black

val onSurface @Composable get() =
    if (getAppState(context = LocalContext.current).darkMode.value ?: isSystemInDarkTheme()) Color(0xFFFFFFFF) else Color(0xFF000000)