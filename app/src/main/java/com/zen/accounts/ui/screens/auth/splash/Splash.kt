package com.zen.accounts.ui.screens.auth.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.zen.accounts.R
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.screens.common.main_route
import com.zen.accounts.ui.screens.common.splash_route
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.background
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.onBackground
import kotlinx.coroutines.launch

@Composable
fun Splash(
    appState: AppState
) {
    val splashScreenAutoCloseCoroutine = rememberCoroutineScope()
    val lottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.logo_animation)
    )

    val progress by animateLottieCompositionAsState(
        composition = lottieComposition,
        iterations = 1,
        speed = 1.5f
    )

    LaunchedEffect(key1 = progress) {
        if(progress == 1f) {
            splashScreenAutoCloseCoroutine.launch {
                appState.navController.navigate(main_route) {
                    this.popUpTo(splash_route) {
                        inclusive
                    }
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
        LottieAnimation(
            composition = lottieComposition,
            progress = progress,
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .background(Color.White)
        )

        Spacer(modifier = Modifier.height(generalPadding))

        Text(
            text = stringResource(id = R.string.app_name),
            textAlign = TextAlign.Center,
            style = Typography.bodyMedium.copy(color = onBackground)
        )
    }
}