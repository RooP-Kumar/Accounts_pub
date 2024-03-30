package com.zen.accounts.ui.screens.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.main_route
import com.zen.accounts.ui.theme.onBackground
import com.zen.accounts.ui.theme.splash_route
import kotlinx.coroutines.launch

@Composable
fun Setting(
    appState: AppState
) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Log Out",
            style = Typography.headlineSmall.copy(color = onBackground),
            modifier = Modifier
                .clickable {
                    coroutineScope.launch {
                        appState.dataStore.logoutUser()
                        appState.authNavController.navigate(main_route){
                            this.popUpTo(splash_route){
                                inclusive
                            }
                        }
                    }
                }
        )
    }
}