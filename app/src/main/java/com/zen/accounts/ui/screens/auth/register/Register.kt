package com.zen.accounts.ui.screens.auth.register

import android.util.Log
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.zen.accounts.ui.screens.common.empty_email
import com.zen.accounts.ui.screens.common.empty_pass
import com.zen.accounts.ui.screens.common.enter_email
import com.zen.accounts.ui.screens.common.enter_name
import com.zen.accounts.ui.screens.common.enter_pass
import com.zen.accounts.ui.screens.common.enter_phone
import com.zen.accounts.ui.screens.common.invalid_email
import com.zen.accounts.ui.screens.common.invalid_pass
import com.zen.accounts.ui.screens.common.login_button_label
import com.zen.accounts.ui.screens.common.register_button_label
import com.zen.accounts.ui.screens.common.required_field
import com.zen.accounts.ui.theme.Purple80
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.background
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.normalPadding
import com.zen.accounts.ui.theme.onBackground
import com.zen.accounts.ui.theme.topBarHeight
import com.zen.accounts.ui.viewmodels.RegisterScreenViewModel
import com.zen.accounts.utility.Utility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class RegisterUiState(
    val userName: MutableState<String> = mutableStateOf(""),
    val email: MutableState<String> = mutableStateOf(""),
    val phone: MutableState<String> = mutableStateOf(""),
    val password: MutableState<String> = mutableStateOf(""),
    val loadingState: MutableState<LoadingState> = mutableStateOf(LoadingState.IDLE),
    val showSnackBar: MutableState<Boolean> = mutableStateOf(false),
    val snackBarText: MutableState<String> = mutableStateOf("")
)

@Composable
fun Register(
    appState: AppState,
    viewModel: RegisterScreenViewModel
) {
    val uiState = viewModel.registerUiState
    val uiStateHolder = viewModel.registerUiStateHolder

    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(key1 = uiState.loadingState.value) {
        when (uiStateHolder.loadingState.value) {
            LoadingState.IDLE -> {}
            LoadingState.LOADING -> {}
            LoadingState.SUCCESS -> {
                viewModel.showSnackBar()
            }

            LoadingState.FAILURE -> {
                viewModel.showSnackBar()
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

    Log.d("asdf", "MainUI: Running")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        TopAppBar(appState = appState)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topBarHeight),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Column(
                modifier = if (screenWidth > 500)
                    Modifier
                        .width(500.dp)
                        .verticalScroll(rememberScrollState())
                else
                    Modifier
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = generalPadding),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "* ",
                        style = Typography.bodySmall.copy(color = Color.Red)
                    )

                    Text(
                        text = required_field,
                        style = Typography.bodySmall.copy(color = onBackground)
                    )
                }

                GeneralEditText(
                    text = uiState.userName.value,
                    onValueChange = { uiState.userName.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholderText = enter_name,
                    keyboardOptions = CustomKeyboardOptions.textEditor
                )

                val emailRequired = remember { Pair(true, mutableStateOf(false)) }
                val emailError = remember { Pair(mutableStateOf(false), invalid_email) }
                GeneralEditText(
                    text = uiState.email.value,
                    required = emailRequired,
                    error = emailError,
                    onValueChange = { uiState.email.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholderText = enter_email,
                    keyboardOptions = CustomKeyboardOptions.emailEditor
                )

                GeneralEditText(
                    text = uiState.phone.value,
                    onValueChange = { uiState.phone.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholderText = enter_phone,
                    keyboardOptions = CustomKeyboardOptions.numberEditor
                )

                val passRequired = remember { Pair(true, mutableStateOf(false)) }
                val passError = remember { Pair(mutableStateOf(false), invalid_pass) }
                GeneralEditText(
                    text = uiState.password.value,
                    required = passRequired,
                    error = passError,
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
                    val emailRegex = """^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$""".toRegex()
                    val passwordRegex = """^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%]).{6,}$""".toRegex()
                    coroutineScope.launch {
                        val email = uiState.email.value
                        val pass = uiState.password.value

                        if(email.trim().isEmpty()) {
                            emailRequired.second.value = true
                        } else if(pass.trim().isEmpty()) {
                            passRequired.second.value = true
                        } else {
                            if(!emailRegex.matches(email)){
                                emailError.first.value = true
                            } else if (!passwordRegex.matches(pass)) {
                                passError.first.value = true
                            } else {
                                viewModel.registerUser(
                                    User(
                                        name = uiState.userName.value,
                                        email = email,
                                        phone = uiState.phone.value
                                    ), pass,
                                    dataStore = appState.dataStore
                                )
                            }
                        }
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