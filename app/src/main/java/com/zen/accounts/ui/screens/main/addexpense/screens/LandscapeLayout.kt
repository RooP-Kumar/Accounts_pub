package com.zen.accounts.ui.screens.main.addexpense.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.zen.accounts.db.model.Expense
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.screens.common.GeneralButton
import com.zen.accounts.ui.screens.common.getRupeeString
import com.zen.accounts.ui.screens.main.addexpense.AddExpenseItemTitleSection
import com.zen.accounts.ui.screens.main.addexpense.ExpenseItemListSection
import com.zen.accounts.ui.screens.main.addexpense.UpperTitleSection
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.halfGeneralPadding
import com.zen.accounts.ui.theme.onSurface
import com.zen.accounts.ui.viewmodels.AddExpenseViewModel
import java.util.Date

@Composable
fun LandscapeLayout(
    appState: AppState,
    viewModel: AddExpenseViewModel
) {
    val uiState = viewModel.addExpenseUiState
    val localDensity = LocalDensity.current
    val allExpenseItem = viewModel.allExpenseItem.collectAsState(initial = arrayListOf())
    val rightSideTitleHeight = remember {
        mutableStateOf(0.dp)
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
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
                    viewModel.addExpenseIntoLocalDatabase(tempExpense)
                    viewModel.deleteExpenseItemsFromLocalDatabase()
                }
            }
        }

    }
}