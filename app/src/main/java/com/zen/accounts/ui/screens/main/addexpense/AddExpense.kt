package com.zen.accounts.ui.screens.main.addexpense

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.zen.accounts.db.model.Expense
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.screens.common.GeneralButton
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.background
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.getRupeeString
import com.zen.accounts.ui.theme.halfGeneralPadding
import com.zen.accounts.ui.theme.onSurface
import java.util.Date


data class AddExpenseUiState(
    val title: MutableState<String> = mutableStateOf(""),
    val expenseItemListAmountTextWidth: MutableState<Dp> = mutableStateOf(0.dp),
    val totalExpenseAmount: MutableState<Long> = mutableStateOf(0L),
)

@Composable
fun AddExpense(
    appState: AppState,
    viewModel: AddExpenseViewModel,
    paddingValues: PaddingValues
) {
    MainUi(viewModel, appState, paddingValues)
}

@Composable
private fun MainUi(
    viewModel: AddExpenseViewModel,
    appState: AppState,
    paddingValues: PaddingValues
) {
    val uiState = viewModel.addExpenseUiState
    val localDensity = LocalDensity.current
    val allExpenseItem = viewModel.allExpenseItem.collectAsState(initial = listOf())
    val screenWidth = LocalConfiguration.current.screenWidthDp

    LaunchedEffect(key1 = allExpenseItem.value.size) {
        if (allExpenseItem.value.isNotEmpty()) {
            allExpenseItem.value.forEach { uiState.totalExpenseAmount.value += it.itemPrice * it.itemQty }
        } else {
            uiState.totalExpenseAmount.value = 0L
        }
    }

    // This for loop is just fro to calculate the maximum width of the text element.
    // Which I can use in the LazyColumn item to set the Rupee Text width.
    allExpenseItem.value.forEach {
        Text(
            text = getRupeeString(it.itemPrice),
            modifier = Modifier
                .onGloballyPositioned {
                    val tempDp = with(localDensity) {
                        it.size.width.toDp()
                    }
                    if (tempDp > uiState.expenseItemListAmountTextWidth.value) {
                        uiState.expenseItemListAmountTextWidth.value = tempDp
                    }
                }
                .zIndex(-1f) // zIndex is -1 so that it does not show on the screen.
        )
    }

    if (screenWidth <= 500) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(background)
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {

            UpperTitleSection(appState = appState, title = uiState.title)

            HorizontalLineOnBackground()

            AddExpenseItemTitleSection(appState = appState)

            ExpenseItemListSection(
                appState = appState,
                paddingValues = paddingValues,
                allExpenseItem = allExpenseItem.value,
                expenseItemListAmountTextWidth = uiState.expenseItemListAmountTextWidth,
                localDensity = localDensity,
                totalExpenseAmount = uiState.totalExpenseAmount,
                addButtonVisible = false,
                title = uiState.title,
                viewModel = viewModel
            )

        }

    } else {

        val rightSideTitleHeight = remember {
            mutableStateOf(0.dp)
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {

            Column(

                modifier = Modifier
                    .weight(1f)

            ) {

                AddExpenseItemTitleSection(
                    appState = appState,
                    orientation = 1
                )

                ExpenseItemListSection(
                    appState = appState,
                    paddingValues = paddingValues,
                    allExpenseItem = allExpenseItem.value,
                    rightSideTitleHeight = rightSideTitleHeight,
                    expenseItemListAmountTextWidth = uiState.expenseItemListAmountTextWidth,
                    orientation = 1,
                    localDensity = localDensity,
                    totalExpenseAmount = uiState.totalExpenseAmount,
                    addButtonVisible = true
                )

            }

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {

                UpperTitleSection(
                    appState = appState,
                    title = uiState.title
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(generalPadding)
                            .padding(horizontal = halfGeneralPadding),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total Amount : ",
                            style = Typography.bodyMedium.copy(color = onSurface)
                        )

                        Text(
                            text = getRupeeString(uiState.totalExpenseAmount.value),
                            style = Typography.bodyMedium.copy(color = onSurface)
                        )
                    }
                    GeneralButton(
                        text = "ADD EXPENSE",
                        modifier = Modifier
                            .padding(bottom = halfGeneralPadding)
                            .align(Alignment.BottomEnd),
                        enable = appState.drawerState?.value != true,
                    ) {

                        val tempExpense = Expense(
                            id = System.currentTimeMillis(),
                            title = uiState.title.value,
                            items = allExpenseItem.value,
                            totalAmount = uiState.totalExpenseAmount.value,
                            date = Date(System.currentTimeMillis())
                        )
                        viewModel.uploadExpense(tempExpense)
                        viewModel.addExpenseIntoLocalDatabase(tempExpense)
                        viewModel.deleteExpenseItemsFromLocalDatabase()
                    }
                }
            }

        }
    }


}