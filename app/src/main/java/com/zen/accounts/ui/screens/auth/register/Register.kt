package com.zen.accounts.ui.screens.auth.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.zen.accounts.db.model.User
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.navigation.Screen
import com.zen.accounts.ui.screens.common.CustomKeyboardOptions
import com.zen.accounts.ui.screens.common.GeneralButton
import com.zen.accounts.ui.screens.common.GeneralEditText
import com.zen.accounts.ui.screens.common.GeneralSnackBar
import com.zen.accounts.ui.screens.common.LoadingDialog
import com.zen.accounts.ui.screens.common.LoadingState
import com.zen.accounts.ui.screens.common.TopAppBar
import com.zen.accounts.ui.screens.common.already_have_account
import com.zen.accounts.ui.screens.common.enter_email
import com.zen.accounts.ui.screens.common.enter_name
import com.zen.accounts.ui.screens.common.enter_pass
import com.zen.accounts.ui.screens.common.enter_phone
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
import com.zen.accounts.ui.theme.topBarHeight
import com.zen.accounts.ui.viewmodels.RegisterScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class RegisterUiState(
    val userName: MutableState<String> = mutableStateOf(""),
    val email: MutableState<String> = mutableStateOf(""),
    val phone: MutableState<String> = mutableStateOf(""),
    val password: MutableState<String> = mutableStateOf(""),
    val loadingState: MutableState<LoadingState> = mutableStateOf(LoadingState.IDLE),
    val showSnackBar : MutableState<Boolean> = mutableStateOf(false),
    val snackBarText : MutableState<String> = mutableStateOf("")
)

@Composable
fun Register(
    appState: AppState,
    viewModel: RegisterScreenViewModel
) {
    val uiState = viewModel.registerUiState
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(key1 = uiState.loadingState.value) {
        when(uiState.loadingState.value) {
            LoadingState.IDLE -> {}
            LoadingState.LOADING -> {}
            LoadingState.SUCCESS -> {
                coroutineScope.launch {
                    uiState.showSnackBar.value = true
                    delay(500)
                    uiState.showSnackBar.value = false
                    appState.navController.navigate(Screen.LoginScreen.route)
                }
            }
            LoadingState.FAILURE -> {
                coroutineScope.launch(Dispatchers.IO) {
                    uiState.showSnackBar.value = true
                    delay(5000)
                    uiState.showSnackBar.value = false
                }
            }
        }
    }
    MainUI(appState = appState, viewModel)
}

@Composable
private fun MainUI(
    appState: AppState,
    viewModel: RegisterScreenViewModel
) {
    val uiState = viewModel.registerUiState
    val coroutineScope = rememberCoroutineScope()
    val screenWidth = LocalConfiguration.current.screenWidthDp
    LoadingDialog(loadingState = uiState.loadingState)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        TopAppBar(appState = appState)

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(top = topBarHeight),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Column(
                modifier = if(screenWidth > 500)
                    Modifier
                        .width(500.dp)
                        .verticalScroll(rememberScrollState())
                else
                    Modifier
            ) {
                GeneralEditText(
                    text = uiState.userName.value,
                    onValueChange = {uiState.userName.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholderText = enter_name,
                    keyboardOptions = CustomKeyboardOptions.textEditor
                )

                GeneralEditText(
                    text = uiState.email.value,
                    onValueChange = {uiState.email.value = it},
                    modifier = Modifier.fillMaxWidth(),
                    placeholderText = enter_email,
                    keyboardOptions = CustomKeyboardOptions.emailEditor
                )

                GeneralEditText(
                    text = uiState.phone.value,
                    onValueChange = {uiState.phone.value = it},
                    modifier = Modifier.fillMaxWidth(),
                    placeholderText = enter_phone,
                    keyboardOptions = CustomKeyboardOptions.numberEditor
                )

                GeneralEditText(
                    text = uiState.password.value,
                    onValueChange = {uiState.password.value = it},
                    modifier = Modifier.fillMaxWidth(),
                    placeholderText = enter_pass,
                    keyboardOptions = CustomKeyboardOptions.passwordEditor
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
                        text = login_button_label,
                        style = Typography.bodySmall.copy(color = Purple80),
                        modifier = Modifier
                            .clickable {
                                appState.navController.popBackStack()
                            }
                    )
                }

                GeneralButton(
                    text = register_button_label
                ) {
                    coroutineScope.launch {
                        viewModel.registerUser(
                            User(
                                name = uiState.userName.value,
                                email = uiState.email.value,
                                phone = uiState.phone.value
                            ), uiState.password.value,
                            dataStore = appState.dataStore
                        )
                    }
                }
            }
        }



        GeneralSnackBar(
            visible = uiState.showSnackBar,
            text = uiState.snackBarText.value,
            modifier = Modifier.align(Alignment.TopCenter)
        )

    }
}

private suspend fun registerUser(appState: AppState, user: User) {
    appState.dataStore.saveUser(user)
}