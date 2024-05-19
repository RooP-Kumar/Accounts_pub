package com.zen.accounts

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.zen.accounts.states.getAppState
import com.zen.accounts.ui.navigation.NavigationGraph
import com.zen.accounts.ui.theme.AccountsThemes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AccountsThemes {
                val navController = rememberNavController()
                val context = LocalContext.current
                val appState = getAppState(context)
                appState.darkMode = appState.dataStore.isInDarkMode.collectAsState(initial = null)
                appState.navController = navController
                NavigationGraph(appState)
            }
        }
    }
}
