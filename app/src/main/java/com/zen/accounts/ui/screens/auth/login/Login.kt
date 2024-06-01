package com.zen.accounts.ui.screens.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.navigation.Screen
import com.zen.accounts.ui.screens.common.CustomKeyboardOptions
import com.zen.accounts.ui.screens.common.GeneralButton
import com.zen.accounts.ui.screens.common.GeneralEditText
import com.zen.accounts.ui.screens.common.GeneralSnackBar
import com.zen.accounts.ui.screens.common.LoadingDialog
import com.zen.accounts.ui.screens.common.LoadingState
import com.zen.accounts.ui.screens.common.did_not_have_account
import com.zen.accounts.ui.screens.common.enter_email
import com.zen.accounts.ui.screens.common.enter_pass
import com.zen.accounts.ui.screens.common.login_button_label
import com.zen.accounts.ui.screens.common.register_button_label
import com.zen.accounts.ui.theme.Purple80
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.background
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.halfGeneralPadding
import com.zen.accounts.ui.theme.normalPadding
import com.zen.accounts.ui.theme.onBackground
import com.zen.accounts.ui.theme.shadowColor
import com.zen.accounts.ui.viewmodels.LoginScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class LoginUiState(
    val emailUsernamePhone: MutableState<String> = mutableStateOf(""),
    val password: MutableState<String> = mutableStateOf(""),
    val loadingState: MutableState<LoadingState> = mutableStateOf(LoadingState.IDLE),
    val snackBarText: MutableState<String> = mutableStateOf(""),
    val showSnackBar: MutableState<Boolean> = mutableStateOf(false)
)

@Composable
fun Login(
    appState: AppState,
    viewModel: LoginScreenViewModel
) {
    val uiState = viewModel.loginUiState
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(key1 = uiState.loadingState.value) {
        when (uiState.loadingState.value) {
            LoadingState.IDLE -> {}
            LoadingState.LOADING -> {}
            LoadingState.SUCCESS -> {
                coroutineScope.launch {
                    uiState.showSnackBar.value = true
                    delay(1000)
                    uiState.showSnackBar.value = false
                    appState.navController.popBackStack()
                }
            }

            LoadingState.FAILURE -> {
                coroutineScope.launch {
                    uiState.showSnackBar.value = true
                    delay(5000)
                    uiState.showSnackBar.value = false
                }
            }
        }
    }

    MainUI(appState, viewModel)
}

@Composable
private fun MainUI(
    appState: AppState,
    viewModel: LoginScreenViewModel
) {
    val uiState = viewModel.loginUiState
    val user = appState.dataStore.getUser.collectAsState(initial = null)
    val coroutineScope = rememberCoroutineScope()

    LoadingDialog(loadingState = uiState.loadingState)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
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
                text = uiState.emailUsernamePhone.value,
                onValueChange = { uiState.emailUsernamePhone.value = it },
                modifier = Modifier.fillMaxWidth(),
                placeholderText = enter_email,
                keyboardOptions = CustomKeyboardOptions.textEditor
            )

            GeneralEditText(
                text = uiState.password.value,
                onValueChange = { uiState.password.value = it },
                modifier = Modifier.fillMaxWidth(),
                placeholderText = enter_pass,
                keyboardOptions = CustomKeyboardOptions.passwordEditor
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = generalPadding, vertical = normalPadding)
            ) {
                Text(
                    text = did_not_have_account,
                    style = Typography.bodySmall.copy(color = onBackground)
                )
                Spacer(modifier = Modifier.width(normalPadding))
                Text(
                    text = register_button_label,
                    style = Typography.bodySmall.copy(color = Purple80),
                    modifier = Modifier
                        .clickable {
                            coroutineScope.launch {
                                appState.navigate(Screen.RegisterScreen.route)
                            }
                        }
                )
            }

            GeneralButton(
                text = login_button_label
            ) {
                val email = uiState.emailUsernamePhone.value.trim()
                val pass = uiState.password.value.trim()
                viewModel.loginUser(email, pass)
            }
        }

        GeneralSnackBar(
            visible = uiState.showSnackBar,
            text = uiState.snackBarText.value,
            modifier = Modifier.align(Alignment.TopCenter)
        )

    }
}
