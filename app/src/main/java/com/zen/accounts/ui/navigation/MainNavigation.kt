package com.zen.accounts.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.screens.common.expense_details_argument
import com.zen.accounts.ui.screens.common.main_route
import com.zen.accounts.ui.screens.main.addexpense.AddExpense
import com.zen.accounts.ui.viewmodels.AddExpenseViewModel
import com.zen.accounts.ui.screens.main.addexpenseitem.AddExpenseItem
import com.zen.accounts.ui.screens.main.expenseDetail.ExpenseDetails
import com.zen.accounts.ui.screens.main.home.Home
import com.zen.accounts.ui.viewmodels.HomeViewModel
import com.zen.accounts.ui.screens.main.myexpense.MyExpense
import com.zen.accounts.ui.viewmodels.MyExpenseViewModel
import com.zen.accounts.ui.screens.main.setting.Setting
import com.zen.accounts.ui.viewmodels.SettingViewModel
import com.zen.accounts.utility.stringToExpense

fun NavGraphBuilder.MainNavigation(appState: AppState) {
    navigation(startDestination = Screen.Home.route, route = main_route) {
        composable(route = Screen.AddExpenseScreen.route) {
            val viewModel: AddExpenseViewModel = hiltViewModel()
            AddExpense(appState = appState, viewModel = viewModel)
        }

        composable(route = Screen.AddExpenseItemScreen.route) {
            val viewModel: AddExpenseViewModel = hiltViewModel()
            AddExpenseItem(appState = appState, viewModel = viewModel)
        }

        // Home Screen
        composable(route = Screen.Home.route) {
            val viewModel: HomeViewModel = hiltViewModel()
            Home(appState = appState, viewModel)
        }

        composable(route = Screen.MyExpenseScreen.route) {
            val viewModel: MyExpenseViewModel = hiltViewModel()
            MyExpense(appState = appState, viewModel)
        }

        composable(
            route = Screen.ExpenseDetailScreen.route,
            arguments = listOf(navArgument(expense_details_argument) {NavType.StringType})
        ) {backStackEntry ->
            val arg = stringToExpense(backStackEntry.arguments?.getString(expense_details_argument)!!)
            val viewModel: MyExpenseViewModel = hiltViewModel()
            ExpenseDetails(appState = appState, arg, viewModel)
        }

        // Setting Screen route to navigate
        composable(route = Screen.SettingScreen.route) {
            val addExpenseViewModel : AddExpenseViewModel = hiltViewModel()
            val settingViewModel : SettingViewModel = hiltViewModel()
            Setting(appState = appState, addExpenseViewModel = addExpenseViewModel, settingViewModel = settingViewModel)
        }
    }
}
