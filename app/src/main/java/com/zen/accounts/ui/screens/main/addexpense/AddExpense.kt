package com.zen.accounts.ui.screens.main.addexpense

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.screens.common.LoadingDialog
import com.zen.accounts.ui.screens.common.LoadingState
import com.zen.accounts.ui.screens.main.addexpense.screens.LandscapeLayout
import com.zen.accounts.ui.screens.main.addexpense.screens.PortraitLayout
import com.zen.accounts.ui.viewmodels.AddExpenseViewModel


data class AddExpenseUiState(
    val title: MutableState<String> = mutableStateOf(""),
    val expenseItemListAmountTextWidth: MutableState<Dp> = mutableStateOf(0.dp),
    val totalExpenseAmount: MutableState<Double> = mutableDoubleStateOf(0.0),
    val loadingState : MutableState<LoadingState> = mutableStateOf(LoadingState.IDLE)
)

@Composable
fun AddExpense(
    appState: AppState,
    viewModel: AddExpenseViewModel
) {
    MainUi(viewModel, appState)
}

@Composable
private fun MainUi(
    viewModel: AddExpenseViewModel,
    appState: AppState
) {
    val uiState = viewModel.addExpenseUiState
    val localDensity = LocalDensity.current
    val allExpenseItem = viewModel.allExpenseItem.collectAsState(initial = listOf())
    val screenWidth = LocalConfiguration.current.screenWidthDp

    LaunchedEffect(key1 = allExpenseItem.value.size) {
        if (allExpenseItem.value.isNotEmpty()) {
            allExpenseItem.value.forEach { uiState.totalExpenseAmount.value += it.itemAmount ?: 0.0 }
        } else {
            uiState.totalExpenseAmount.value = 0.0
        }
    }

    LoadingDialog(
        loadingState = uiState.loadingState,
        onSuccess = {
            viewModel.deleteExpenseItemsFromLocalDatabase()
            uiState.title.value = ""
        }
    )

    if (screenWidth <= 500) {
        PortraitLayout(appState = appState, viewModel = viewModel)
    } else {
        LandscapeLayout(appState = appState, viewModel = viewModel)
    }
}