package com.zen.accounts.ui.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.theme.background
import com.zen.accounts.ui.theme.main_route
import com.zen.accounts.ui.theme.onBackground
import com.zen.accounts.ui.theme.splash_route
import com.zen.accounts.ui.theme.splash_screen_route
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Splash(
    appState: AppState
) {
    val splashScreenAutoCloseCoroutine = rememberCoroutineScope()
    LaunchedEffect(key1 = Unit) {
        splashScreenAutoCloseCoroutine.launch {
            delay(1000L)
            appState.authNavController.navigate(main_route){
                this.popUpTo(splash_route) {
                    inclusive
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = splash_screen_route,
            style = TextStyle.Default.copy(
                color = onBackground
            )
        )
    }
}