package com.zen.accounts.ui.screens.auth.login

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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.zen.accounts.CommonUIStateHolder
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.navigation.Screen
import com.zen.accounts.ui.screens.common.CustomKeyboardOptions
import com.zen.accounts.ui.screens.common.GeneralButton
import com.zen.accounts.ui.screens.common.GeneralEditText
import com.zen.accounts.ui.screens.common.GeneralSnackBar
import com.zen.accounts.ui.screens.common.LoadingDialog
import com.zen.accounts.ui.screens.common.LoadingState
import com.zen.accounts.ui.screens.common.TopAppBar
import com.zen.accounts.ui.screens.common.did_not_have_account
import com.zen.accounts.ui.screens.common.enter_email
import com.zen.accounts.ui.screens.common.enter_pass
import com.zen.accounts.ui.screens.common.invalid_email
import com.zen.accounts.ui.screens.common.invalid_pass
import com.zen.accounts.ui.screens.common.login_button_label
import com.zen.accounts.ui.screens.common.register_button_label
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.normalPadding
import com.zen.accounts.ui.theme.primary_color
import com.zen.accounts.ui.theme.topBarHeight
import com.zen.accounts.ui.viewmodels.LoginScreenViewModel
import kotlinx.coroutines.launch

@Composable
fun Login(
    appState: AppState, viewModel: LoginScreenViewModel
) {
    val loginUiStateHolder = viewModel.loginUiStateHolder.collectAsState()
    val commonUIStateHolder = viewModel.commonUIStateHolder.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(key1 = loginUiStateHolder.value.loadingState) {
        when (loginUiStateHolder.value.loadingState) {
            LoadingState.IDLE -> {}
            LoadingState.LOADING -> {}
            LoadingState.SUCCESS -> {
                viewModel.showSnackBar()
                coroutineScope.launch {
                    appState.navController.popBackStack()
                }
            }

            LoadingState.FAILURE -> {
                viewModel.showSnackBar()
            }
        }
    }

    MainUI(appState, viewModel, commonUIStateHolder, loginUiStateHolder)
}

@Composable
private fun MainUI(
    appState: AppState,
    viewModel: LoginScreenViewModel,
    commonUIStateHolder: State<CommonUIStateHolder>,
    loginUiStateHolder: State<LoginUiStateHolder>
) {
    val coroutineScope = rememberCoroutineScope()
    val screenWidth = LocalConfiguration.current.screenWidthDp

    LoadingDialog(loadingState = loginUiStateHolder.value.loadingState)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {

        TopAppBar(appState = appState)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .padding(top = topBarHeight),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Column(
                modifier = if (screenWidth > 500) Modifier.width(500.dp) else Modifier
            ) {
                GeneralEditText(
                    text = loginUiStateHolder.value.emailUsernamePhone,
                    error = Pair(loginUiStateHolder.value.emailError, invalid_email),
                    required = true,
                    onValueChange = {
                        viewModel.onTextFieldValueChange(it, loginUiStateHolder_emailOrPhone)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholderText = enter_email,
                    keyboardOptions = CustomKeyboardOptions.textEditor
                )

                GeneralEditText(
                    text = loginUiStateHolder.value.password,
                    error = Pair(loginUiStateHolder.value.passError, invalid_pass),
                    required = true,
                    onValueChange = {
                        viewModel.onTextFieldValueChange(it, loginUiStateHolder_pass)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholderText = enter_pass,
                    keyboardOptions = CustomKeyboardOptions.passwordEditor
                )

                Row(
                    modifier = Modifier.padding(
                        horizontal = generalPadding,
                        vertical = normalPadding
                    )
                ) {
                    Text(
                        text = did_not_have_account,
                        style = Typography.bodySmall.copy(color = MaterialTheme.colors.onBackground)
                    )
                    Spacer(modifier = Modifier.width(normalPadding))
                    Text(text = register_button_label,
                        style = Typography.bodySmall.copy(color = primary_color),
                        modifier = Modifier.clickable {
                            coroutineScope.launch {
                                appState.navigate(Screen.RegisterScreen.route)
                            }
                        })
                }

                GeneralButton(
                    text = login_button_label,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = generalPadding)
                ) {
                    viewModel.loginUser()
                }
            }
        }

        GeneralSnackBar(
            visible = commonUIStateHolder.value.showSnackBar,
            text = commonUIStateHolder.value.snackBarText,
            modifier = Modifier.align(Alignment.TopCenter)
        )

    }
}
