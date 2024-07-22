package com.zen.accounts.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.screens.common.expense_details_argument
import com.zen.accounts.ui.screens.common.main_route
import com.zen.accounts.ui.screens.main.add_expense_item.AddExpenseItem
import com.zen.accounts.ui.screens.main.addexpense.AddExpense
import com.zen.accounts.ui.screens.main.expense_detail.ExpenseDetails
import com.zen.accounts.ui.screens.main.home.Home
import com.zen.accounts.ui.screens.main.myexpense.MyExpense
import com.zen.accounts.ui.screens.main.setting.Setting
import com.zen.accounts.ui.theme.tweenAnimDuration
import com.zen.accounts.ui.viewmodels.AddExpenseViewModel
import com.zen.accounts.ui.viewmodels.ExpenseDetailsViewModel
import com.zen.accounts.ui.viewmodels.HomeViewModel
import com.zen.accounts.ui.viewmodels.MyExpenseViewModel
import com.zen.accounts.ui.viewmodels.SettingViewModel
import com.zen.accounts.utility.stringToExpense

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.MainNavigation(appState: AppState) {
    navigation(startDestination = Screen.Home.route, route = main_route) {
        composable(
            route = Screen.AddExpenseScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(durationMillis = tweenAnimDuration)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(tweenAnimDuration)
                )
            },
            popEnterTransition = {
                null
            }
        ) {
            val viewModel: AddExpenseViewModel = hiltViewModel()
            AddExpense(
                appState = appState,
                viewModel = viewModel,
                appState.navController::navigateUp,
                getScreenRouteWithTitle().find { it.route == appState.navController.currentDestination?.route })
        }

        composable(
            route = Screen.AddExpenseItemScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(durationMillis = tweenAnimDuration)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(tweenAnimDuration)
                )
            },
            popEnterTransition = {
                null
            }
        ) {
            val viewModel: AddExpenseViewModel = hiltViewModel()
            AddExpenseItem(
                viewModel = viewModel,
                getScreenRouteWithTitle().find { it.route == appState.navController.currentDestination?.route },
                appState.navController::popBackStack
            )
        }

        // Home Screen
        composable(
            route = Screen.Home.route
        ) {
            val viewModel: HomeViewModel = hiltViewModel()
            val settingViewModel: SettingViewModel = hiltViewModel()
            Home(appState = appState, viewModel, settingViewModel)
        }

        composable(
            route = Screen.MyExpenseScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(durationMillis = tweenAnimDuration)
                )
            },
            popEnterTransition = {
                null
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(tweenAnimDuration)
                )
            }
        ) {
            val viewModel: MyExpenseViewModel = hiltViewModel()
            MyExpense(
                drawerState = appState.drawerState,
                viewModel,
                isMonthlyExpense = false,
                navigateUp = appState.navController::navigateUp,
                getScreenRouteWithTitle().find { it.route == appState.navController.currentDestination?.route },
                navigateTo = {
                    appState.navigate(it)
                })
        }

        composable(
            route = Screen.ExpenseDetailScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(durationMillis = tweenAnimDuration)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(tweenAnimDuration)
                )
            },
            popEnterTransition = {
                null
            },
            arguments = listOf(navArgument(expense_details_argument) { NavType.StringType })
        ) { backStackEntry ->
            val arg =
                stringToExpense(backStackEntry.arguments?.getString(expense_details_argument)!!)
            val expenseDetailsViewModel: ExpenseDetailsViewModel = hiltViewModel()
            ExpenseDetails(appState.drawerState,
                arg,
                expenseDetailsViewModel,
                appState.navController::navigateUp,
                getScreenRouteWithTitle().find { it.route == appState.navController.currentDestination?.route })
        }

        composable(
            route = Screen.MonthlyExpenseScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(durationMillis = tweenAnimDuration)
                )
            },
            popEnterTransition = {
                null
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(tweenAnimDuration)
                )
            }
        ) {
            val viewModel: MyExpenseViewModel = hiltViewModel()
            MyExpense(
                drawerState = appState.drawerState,
                viewModel,
                isMonthlyExpense = true,
                navigateUp = appState.navController::navigateUp,
                getScreenRouteWithTitle().find { it.route == appState.navController.currentDestination?.route },
                navigateTo = {
                    appState.navigate(it)
                }
            )
        }

        // Setting Screen route to navigate
        composable(
            route = Screen.SettingScreen.route,
            enterTransition = {
                slideInHorizontally(tween(tweenAnimDuration)) { it }
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(tweenAnimDuration)
                )
            },
            popEnterTransition = {
                null
            }
        ) {
            val settingViewModel: SettingViewModel = hiltViewModel()
            Setting(
                settingViewModel = settingViewModel,
                appState.navController::navigateUp,
                getScreenRouteWithTitle().find { it.route == appState.navController.currentDestination?.route }
            )
        }
    }
}
