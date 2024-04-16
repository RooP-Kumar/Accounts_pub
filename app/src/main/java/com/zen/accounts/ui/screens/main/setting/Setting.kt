package com.zen.accounts.ui.screens.main.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.screens.common.GeneralButton
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.auth_route
import com.zen.accounts.ui.theme.background
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.login_button_label
import com.zen.accounts.ui.theme.login_label
import com.zen.accounts.ui.theme.onBackground
import kotlinx.coroutines.launch

@Composable
fun Setting(
    appState: AppState
) {
    val coroutineScope = rememberCoroutineScope()
    val darkSwitchOn = appState.darkMode.value ?: isSystemInDarkTheme()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(generalPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Dark Mode",
                style = Typography.bodyMedium.copy(color = onBackground)
            )

            Switch(
                checked = darkSwitchOn,
                onCheckedChange = {
                    coroutineScope.launch {
                        appState.dataStore.saveIsDarkMode(!darkSwitchOn)
                    }
                }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = generalPadding, bottom = generalPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = login_label,
                style = Typography.bodyMedium.copy(color = onBackground)
            )

            GeneralButton(
                text = login_button_label,
                modifier = Modifier
            ) {
                appState.authNavController.navigate(auth_route)
            }
        }
    }
}