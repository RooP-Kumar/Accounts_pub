package com.zen.accounts.ui.screens.main.expenseDetail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.zen.accounts.db.model.Expense
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.screens.common.GeneralButton
import com.zen.accounts.ui.screens.common.TopAppBar
import com.zen.accounts.ui.screens.common.getRupeeString
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.background
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.halfGeneralPadding
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

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = expense.title,
                    textAlign = TextAlign.Center,
                    style = Typography.bodyLarge.copy(color = onBackground),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(halfGeneralPadding))

                Text(
                    text = getRupeeString(expense.totalAmount),
                    textAlign = TextAlign.Center,
                    style = Typography.bodyLarge.copy(color = onBackground),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(generalPadding)
            ) {
                GeneralButton(
                    text = "EDIT",
                    modifier = Modifier
                        .weight(1f),
                    containerColor = Color.Green.copy(alpha = 0.5f)
                ) {

                }

                Spacer(modifier = Modifier.width(generalPadding))

                GeneralButton(
                    text = "DELETE",
                    modifier = Modifier
                        .weight(1f),
                    containerColor = Color.Red.copy(alpha = 0.5f)
                ) {

                }
            }

            HorizontalDivider()

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(generalPadding)
            ) {
                items(expense.items.size) { expenseItemInd ->
                    ExpenseItemLayout(
                        expenseItem = expense.items[expenseItemInd],
                        textStyle = Typography.bodyMedium.copy(color = onSurface)
                    )
                }
            }
        }
    }
}