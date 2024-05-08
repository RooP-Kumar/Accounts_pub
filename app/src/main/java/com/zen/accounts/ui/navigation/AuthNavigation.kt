package com.zen.accounts.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.screens.auth.login.Login
import com.zen.accounts.ui.viewmodels.LoginScreenViewModel
import com.zen.accounts.ui.screens.auth.register.Register
import com.zen.accounts.ui.viewmodels.RegisterScreenViewModel
import com.zen.accounts.ui.screens.common.auth_route

fun NavGraphBuilder.AuthNavigation(
    appState: AppState
) {
    navigation(startDestination = Screen.LoginScreen.route, route = auth_route) {
        composable(route = Screen.LoginScreen.route) {
            val viewmodel = hiltViewModel<LoginScreenViewModel>()
            Login(appState, viewmodel)
        }

        composable(route = Screen.RegisterScreen.route) {
            val viewmodel : RegisterScreenViewModel = hiltViewModel()
            Register(appState, viewmodel)
        }
    }
}