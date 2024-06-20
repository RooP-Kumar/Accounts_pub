package com.zen.accounts.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.screens.auth.login.Login
import com.zen.accounts.ui.screens.auth.register.Register
import com.zen.accounts.ui.screens.common.auth_route
import com.zen.accounts.ui.theme.tweenAnimDuration
import com.zen.accounts.ui.viewmodels.LoginScreenViewModel
import com.zen.accounts.ui.viewmodels.RegisterScreenViewModel

fun NavGraphBuilder.AuthNavigation(
    appState: AppState
) {
    navigation(startDestination = Screen.LoginScreen.route, route = auth_route) {
        composable(
            route = Screen.LoginScreen.route,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(durationMillis = tweenAnimDuration))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(tweenAnimDuration))
            },
            popEnterTransition = {
                null
            }
        ) {
            val viewmodel = hiltViewModel<LoginScreenViewModel>()
            Login(appState, viewmodel)
        }

        composable(
            route = Screen.RegisterScreen.route,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(durationMillis = tweenAnimDuration))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(tweenAnimDuration))
            },
            popEnterTransition = {
                null
            }) {
            val viewmodel : RegisterScreenViewModel = hiltViewModel()
            Register(appState, viewmodel)
        }
    }
}