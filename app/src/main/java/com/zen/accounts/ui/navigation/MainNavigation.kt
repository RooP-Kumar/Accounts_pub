package com.zen.accounts.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.screens.addexpense.AddExpense
import com.zen.accounts.ui.screens.addexpense.AddExpenseViewModel
import com.zen.accounts.ui.screens.myexpense.MyExpense
import com.zen.accounts.ui.screens.setting.Setting
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.main_route

@Composable
fun MainNavigation(appState: AppState, padding : PaddingValues) {
    appState.mainNavController = rememberNavController()
    NavHost(navController = appState.mainNavController, startDestination = BottomScreen.AddExpenseScreen.route, route = main_route){
        composable(route = BottomScreen.AddExpenseScreen.route) {
            val viewModel : AddExpenseViewModel = hiltViewModel()
            AddExpense(appState = appState, viewModel = viewModel)
        }

        composable(route = BottomScreen.MyExpenseScreen.route) {
            MyExpense(appState = appState)
        }

        composable(route = BottomScreen.SettingScreen.route) {
            Setting(appState = appState)
        }
    }
}

@Composable
fun BottomNavigation(appState: AppState) {
    val items = listOf(
        BottomScreen.MyExpenseScreen,
        BottomScreen.AddExpenseScreen,
        BottomScreen.SettingScreen
    )
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by appState.mainNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(painter = painterResource(id = screen.icon), contentDescription = null) },
                        label = {
                            Text(
                                screen.title,
                                style = Typography.bodySmall
                            )
                        },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            appState.mainNavController.navigate(screen.route) {
                                popUpTo(appState.mainNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) {
        MainNavigation(appState = appState, it)
    }
}