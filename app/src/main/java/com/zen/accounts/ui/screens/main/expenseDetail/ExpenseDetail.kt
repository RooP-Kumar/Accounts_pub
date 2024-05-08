package com.zen.accounts.ui.screens.main.expenseDetail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zen.accounts.db.model.Expense
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.screens.common.TopAppBar
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.background
import com.zen.accounts.ui.theme.onBackground
import com.zen.accounts.ui.theme.onSurface
import com.zen.accounts.ui.viewmodels.MyExpenseViewModel
import com.zen.accounts.utility.ExpenseItemLayout

@Composable
fun ExpenseDetails(
    appState : AppState,
    expense: Expense,
    myExpenseViewModel: MyExpenseViewModel
) {
    MainUI(
        appState,
        expense,
        myExpenseViewModel
    )
}

@Composable
private fun MainUI(
    appState: AppState,
    expense: Expense,
    myExpenseViewModel: MyExpenseViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(appState = appState)
            Text(
                text = expense.title,
                style = Typography.bodyMedium.copy(color = onBackground),
                modifier = Modifier
                    .fillMaxWidth()
            )

            Text(
                text = "Items",
                style = Typography.bodyMedium.copy(color = onBackground),
                modifier = Modifier
                    .fillMaxWidth()
            )

            repeat(expense.items.size) { expenseItemInd ->
                ExpenseItemLayout(
                    expenseItem = expense.items[expenseItemInd],
                    textStyle = Typography.bodyMedium.copy(color = onSurface)
                )

            }
        }
    }
}