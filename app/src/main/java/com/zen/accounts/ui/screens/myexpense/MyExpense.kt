package com.zen.accounts.ui.screens.myexpense

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.zen.accounts.db.model.ExpenseItem
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.component.GeneralButton
import com.zen.accounts.ui.screens.addexpense.AddExpenseViewModel
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.background
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.getRupeeString
import com.zen.accounts.ui.theme.halfGeneralPadding
import com.zen.accounts.ui.theme.onBackground
import com.zen.accounts.ui.theme.onSurface
import com.zen.accounts.ui.theme.shadowColor
import com.zen.accounts.ui.theme.surface
import com.zen.accounts.utility.ExpenseItemLayout
import com.zen.accounts.utility.customShadow

data class MyExpenseUiState(
    val expenseItemListAmountTextWidth : MutableState<Dp> = mutableStateOf(0.dp)
)

@Composable
fun MyExpense(
    appState: AppState,
    viewModel: MyExpenseViewModel,
    paddingValue: PaddingValues
) {
    val localDensity = LocalDensity.current
    val uiState = viewModel.myExpenseUiState
    val allExpense = viewModel.allExpenseFromApi.collectAsState(initial = listOf())

    // This for loop is just fro to calculate the maximum width of the text element.
    // Which I can use in the LazyColumn item to set the Rupee Text width.
    allExpense.value.forEach {expense ->
        expense.items.forEach {
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
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValue.calculateTopPadding(),
                bottom = paddingValue.calculateBottomPadding()
            )
            .background(background)
    ) {
        androidx.compose.animation.AnimatedVisibility(
            visible = allExpense.value.isEmpty(),
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            Text(
                text = "No Expenses",
                style = Typography.headlineSmall.copy(color = onSurface),
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }

        androidx.compose.animation.AnimatedVisibility(
            visible = allExpense.value.isNotEmpty(),
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = generalPadding)
                    .padding(top = generalPadding)
            ) {

                items(allExpense.value.size, key = { it.hashCode() }) {

                    Column(

                        modifier = Modifier
                            .padding(vertical = halfGeneralPadding)
                            .customShadow()
                            .background(background)
                            .padding(generalPadding)

                    ) {

                        Row(

                            modifier = Modifier
                                .fillMaxWidth()

                        ) {

                            Text(
                                text = allExpense.value[it].title,
                                style = Typography.bodyLarge.copy(color = onBackground),
                                modifier = Modifier
                                    .weight(1f)
                            )

                            Text(
                                text = getRupeeString(allExpense.value[it].totalAmount),
                                style = Typography.bodyLarge.copy(color = onBackground)
                            )

                        }

                        Column(

                            modifier = Modifier
                                .padding(top = generalPadding)
                                .clip(shape = RoundedCornerShape(halfGeneralPadding))
                                .background(surface)
                                .padding(halfGeneralPadding)

                        ) {

                            repeat(allExpense.value[it].items.size) { expenseItemInd ->

                                ExpenseItemLayout(
                                    expenseItem = allExpense.value[it].items[expenseItemInd],
                                    uiState.expenseItemListAmountTextWidth,
                                    textStyle = Typography.bodyMedium.copy(color = onSurface),
                                    qtyTextStyle = Typography.bodySmall.copy(color = onSurface)
                                )

                            }

                        }

                    }

                }

            }

        }

        GeneralButton(text = "pull data", modifier = Modifier.fillMaxWidth()) {
            viewModel.getAllExpenseFromApi()
        }

    }

}