package com.zen.accounts.ui.screens.main.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.screens.main.home.landscape.HomeLandscapeScreen
import com.zen.accounts.ui.screens.main.home.portrait.HomePortraitScreen
import com.zen.accounts.ui.viewmodels.HomeViewModel
import com.zen.accounts.ui.viewmodels.SettingViewModel

data class HomeUiState(
    val totalAmount : MutableState<Double> = mutableDoubleStateOf(0.0)
)

@Composable
fun Home(
    appState: AppState,
    viewModel: HomeViewModel,
    settingViewModel: SettingViewModel
) {
    val uiState = viewModel.homeUiState
    val monthlyExpense = viewModel.monthlyExpense.collectAsState(initial = listOf())
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    DisposableEffect(key1 = monthlyExpense.value.size) {
        if(monthlyExpense.value.isNotEmpty())
            monthlyExpense.value.forEach {
                uiState.totalAmount.value += it.totalAmount
            }
        onDispose { uiState.totalAmount.value = 0.0 }
    }

    if(screenWidth <= 500.dp) {
        HomePortraitScreen(appState = appState, viewModel = viewModel)
    } else {
        HomeLandscapeScreen(appState = appState, viewModel = viewModel, settingViewModel)
    }
}
