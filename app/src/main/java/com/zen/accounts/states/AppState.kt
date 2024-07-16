package com.zen.accounts.states

import android.content.Context
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHostController
import com.zen.accounts.db.datastore.UserDataStore
import com.zen.accounts.ui.screens.common.home_screen_route

@Stable
class AppState(context: Context) {

    // Holding navController for circulate throughout the whole application.
    var navController : NavHostController = NavHostController(context)

    fun navigate(route : String) {
        navController.navigate(route) {
            launchSingleTop = true
            restoreState = true
            popUpTo(route) {
                inclusive = true
            }
        }
    }

    fun drawerNavigate(route : String) {
        navController.navigate(route) {
            launchSingleTop = true
            restoreState = true
            popUpTo(home_screen_route) {
                inclusive = false
            }
        }
    }

    // User Data Store
    var dataStore = UserDataStore(context)

    // Drawer State
    var drawerState : MutableState<DrawerState?> = mutableStateOf(null)

    // Dark Mode
    var darkMode : MutableState<Boolean> = mutableStateOf(false)
}