package com.zen.accounts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.zen.accounts.states.getAppState
import com.zen.accounts.ui.navigation.NavigationGraph
import com.zen.accounts.ui.theme.AccountsThemes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AccountsThemes {
                val navController = rememberNavController()
                val context = LocalContext.current
                val appState = getAppState(context)
                appState.authNavController = navController
                NavigationGraph(appState)
            }
        }
    }
}
