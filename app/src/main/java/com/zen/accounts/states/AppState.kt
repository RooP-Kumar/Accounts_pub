package com.zen.accounts.states

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.zen.accounts.db.datastore.UserDataStore

@Composable
@Stable
fun getAppState(context: Context): AppState {
    return AppState.getAppState(context)
}

@Stable
class AppState(context: Context) {
    // Companion object and instance. to Create singleton object for AppState.
    companion object {
        private var instance : AppState? = null
        fun getAppState(context: Context) : AppState {
            return if (instance == null) {
                instance = AppState(context)
                instance!!
            } else {
                instance!!
            }
        }
    }

    // Holding navController for circulate throughout the whole application.
    var authNavController : NavHostController = NavHostController(context)
    var mainNavController : NavHostController = NavHostController(context)

    // User Data Store
    var dataStore = UserDataStore(context)

    // Drawer State
    var drawerState : MutableState<Boolean>? = null

    // Dark Mode
    lateinit var darkMode : State<Boolean?>
}