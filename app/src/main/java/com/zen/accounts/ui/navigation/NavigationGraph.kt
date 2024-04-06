package com.zen.accounts.ui.navigation

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.zen.accounts.R
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.screens.splash.Splash
import com.zen.accounts.ui.theme.add_expense_item_screen_route
import com.zen.accounts.ui.theme.add_expense_screen_label
import com.zen.accounts.ui.theme.add_expense_screen_route
import com.zen.accounts.ui.theme.login_screen_route
import com.zen.accounts.ui.theme.main_route
import com.zen.accounts.ui.theme.my_expense_screen_label
import com.zen.accounts.ui.theme.my_expense_screen_route
import com.zen.accounts.ui.theme.register_screen_route
import com.zen.accounts.ui.theme.setting_screen_label
import com.zen.accounts.ui.theme.setting_screen_route
import com.zen.accounts.ui.theme.splash_route
import com.zen.accounts.ui.theme.splash_screen_route

@Composable
fun NavigationGraph(
    appState: AppState
) {
    NavHost(navController = appState.authNavController, startDestination = Screen.SplashScreen.route, route = splash_route){
        composable(route = Screen.SplashScreen.route) {
            Splash(appState)
        }

        AuthNavigation(appState)

        composable(route = main_route) {
            BottomNavigation(appState = appState)
        }



    }
}

sealed class Screen(val route : String, val title: String) {
    data object SplashScreen : Screen(splash_screen_route, "Splash Screen")
    data object LoginScreen : Screen(login_screen_route, "Login")
    data object RegisterScreen : Screen(register_screen_route, "Register")

    data object AddExpenseItemScreen : Screen(add_expense_item_screen_route, "Add Expense Item")
}

sealed class BottomScreen(val route: String, val title: String, @DrawableRes val icon : Int) {
    data object AddExpenseScreen : BottomScreen(add_expense_screen_route, add_expense_screen_label, R.drawable.ic_add_expense)
    data object MyExpenseScreen : BottomScreen(my_expense_screen_route, my_expense_screen_label, R.drawable.ic_book)
    data object SettingScreen : BottomScreen(setting_screen_route, setting_screen_label, R.drawable.ic_setting)
}

fun getAllScreenRouteWithTitle() : List<Pair<String, String>> {
    return listOf(
        Pair(BottomScreen.AddExpenseScreen.route, BottomScreen.AddExpenseScreen.title),
        Pair(BottomScreen.MyExpenseScreen.route, BottomScreen.MyExpenseScreen.title),
        Pair(BottomScreen.SettingScreen.route, BottomScreen.SettingScreen.title),
        Pair(Screen.AddExpenseItemScreen.route, Screen.AddExpenseItemScreen.title)
    )
}