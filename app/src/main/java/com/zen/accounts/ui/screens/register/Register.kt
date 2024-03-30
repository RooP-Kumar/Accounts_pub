package com.zen.accounts.ui.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.input.KeyboardType
import com.zen.accounts.db.model.User
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.component.GeneralButton
import com.zen.accounts.ui.component.GeneralEditText
import com.zen.accounts.ui.navigation.Screen
import com.zen.accounts.ui.theme.Purple80
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.already_have_account
import com.zen.accounts.ui.theme.background
import com.zen.accounts.ui.theme.enter_email
import com.zen.accounts.ui.theme.enter_name
import com.zen.accounts.ui.theme.enter_pass
import com.zen.accounts.ui.theme.enter_phone
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.halfGeneralPadding
import com.zen.accounts.ui.theme.login_label_cap
import com.zen.accounts.ui.theme.normalPadding
import com.zen.accounts.ui.theme.onBackground
import com.zen.accounts.ui.theme.register_label_cap
import com.zen.accounts.ui.theme.shadowColor
import kotlinx.coroutines.launch

data class RegisterUiState (
    val userName : MutableState<String> = mutableStateOf(""),
    val email : MutableState<String> = mutableStateOf(""),
    val phone : MutableState<String> = mutableStateOf(""),
    val password : MutableState<String> = mutableStateOf(""),
)

@Composable
fun Register(
    appState: AppState,
    viewModel: RegisterScreenViewModel
) {
    MainUI(appState = appState, viewModel)
}

@Composable
private fun MainUI(
    appState: AppState,
    viewModel: RegisterScreenViewModel
) {
    val uiState = viewModel.registerUiState
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = generalPadding)
                .shadow(
                    elevation = halfGeneralPadding,
                    shape = RoundedCornerShape(generalPadding),
                    ambientColor = shadowColor,
                    spotColor = shadowColor
                )
                .background(background)
                .padding(vertical = halfGeneralPadding)
        ) {
            GeneralEditText(
                text = uiState.userName,
                modifier = Modifier.fillMaxWidth(),
                placeholderText = enter_name
            )

            GeneralEditText(
                text = uiState.email,
                modifier = Modifier.fillMaxWidth(),
                placeholderText = enter_email
            )

            GeneralEditText(
                text = uiState.phone,
                modifier = Modifier.fillMaxWidth(),
                placeholderText = enter_phone,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Phone
                )
            )

            GeneralEditText(
                text = uiState.password,
                modifier = Modifier.fillMaxWidth(),
                placeholderText = enter_pass,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                )
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = generalPadding, vertical = normalPadding)
            ) {
                Text(
                    text = already_have_account,
                    style = Typography.bodySmall.copy(color = onBackground)
                )
                Spacer(modifier = Modifier.width(normalPadding))
                Text(
                    text = login_label_cap,
                    style = Typography.bodySmall.copy(color = Purple80),
                    modifier = Modifier
                        .clickable {
                            appState.authNavController.navigate(Screen.LoginScreen.route)
                        }
                )
            }

            GeneralButton(text = register_label_cap, modifier = Modifier) {
                coroutineScope.launch {
                    registerUser(appState, User(uiState.userName.value, uiState.email.value, uiState.phone.value))
                }
            }
        }

    }
}

private suspend fun registerUser(appState: AppState, user: User) {
    appState.dataStore.saveUser(user)
}