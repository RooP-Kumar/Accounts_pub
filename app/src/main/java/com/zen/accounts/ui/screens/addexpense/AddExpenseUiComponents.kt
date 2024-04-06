package com.zen.accounts.ui.screens.addexpense

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zen.accounts.db.model.Expense
import com.zen.accounts.db.model.ExpenseItem
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.component.GeneralButton
import com.zen.accounts.ui.component.GeneralEditText
import com.zen.accounts.ui.navigation.Screen
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
import java.util.Date

@Composable
fun UpperTitleSection(
    appState: AppState,
    title : MutableState<String>
) {
    Text(
        text = "Add Title",
        style = Typography.titleSmall.copy(color = onBackground),
        modifier = Modifier
            .padding(top = halfGeneralPadding)
            .padding(vertical = halfGeneralPadding, horizontal = generalPadding)
    )

    HorizontalLineOnBackground()

    GeneralEditText(
        text = title,
        modifier = Modifier,
        placeholderText = "Enter Title",
        enable = appState.drawerState?.value != true,
        showClickEffect = false
    )
}

@Composable
fun AddExpenseItemTitleSection(
    appState : AppState,
    orientation: Int = 0
) {
    var tempModifier : Modifier
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        tempModifier = if (orientation == 0) {
            Modifier
                .padding(vertical = halfGeneralPadding, horizontal = generalPadding)
                .weight(1f)
        } else {
            Modifier
                .padding(top = halfGeneralPadding)
                .padding(vertical = halfGeneralPadding, horizontal = generalPadding)
        }
        Text(
            text = "Add Expense Items",
            style = Typography.titleSmall.copy(color = onBackground),
            modifier = tempModifier
        )

        if(orientation ==0) {
            AddExpenseButton(
                appState = appState,
                orientation = 0
            )
        }
    }

    HorizontalLineOnBackground()
}

@Composable
fun ExpenseItemListSection (
    appState : AppState,
    paddingValues: PaddingValues,
    allExpenseItem : List<ExpenseItem>,
    rightSideTitleHeight : MutableState<Dp>? = null,
    expenseItemListAmountTextWidth : MutableState<Dp>,
    localDensity: Density,
    title : MutableState<String>? = null,
    viewModel: AddExpenseViewModel? = null,
    totalExpenseAmount : MutableState<Long>,
    orientation: Int = 0,
    addButtonVisible : Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(surface)
    ) {

        AnimatedVisibility(
            visible = allExpenseItem.isEmpty(),
            modifier = Modifier
                .align(Alignment.Center)
        ) {

            Text(

                text = "No Expenses",
                style = Typography.headlineSmall.copy(
                    color = onSurface
                )

            )

        }

        AnimatedVisibility(
            visible = allExpenseItem.isNotEmpty()
        ) {

            if (orientation == 0) {
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumSection(
                        allExpenseItem,
                        expenseItemListAmountTextWidth
                    )
                    Column(
                        modifier = Modifier
                            .background(background)
                            .padding(bottom = halfGeneralPadding)
                            .align(Alignment.BottomCenter)
                    ) {

                        Text(
                            text = getRupeeString(totalExpenseAmount.value),
                            textAlign = TextAlign.End,
                            style = Typography.bodyMedium.copy(color = onSurface),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = generalPadding, end = generalPadding)
                        )

                        GeneralButton(

                            text = "ADD EXPENSE",
                            enable = appState.drawerState?.value != true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = halfGeneralPadding)

                        ) {
                            val tempExpense = Expense(
                                id = System.currentTimeMillis(),
                                title = title!!.value,
                                items = allExpenseItem,
                                totalAmount = totalExpenseAmount.value,
                                date = System.currentTimeMillis().toString()
                            )
                            viewModel!!.addExpenseIntoLocalDatabase(tempExpense)
                        }

                    }
                }
            } else {
                LazyColumSection(
                    allExpenseItem,
                    expenseItemListAmountTextWidth
                )
            }
        }

        AddExpenseButton(
            modifier = Modifier.align(Alignment.BottomEnd),
            appState = appState,
            rightSideTitleHeight,
            localDensity,
            orientation = 1,
            addButtonVisible
        )

    }

}

@Composable
fun LazyColumSection(
    allExpenseItem : List<ExpenseItem>,
    expenseItemListAmountTextWidth: MutableState<Dp>
) {
    LazyColumn(
        modifier = Modifier
            .padding(generalPadding)

    ) {
        items(allExpenseItem.size + 1, key = { it.hashCode() }) {
            ExpenseItemLayout(
                expenseItem = if (it < allExpenseItem.size) allExpenseItem[it] else ExpenseItem(),
                expenseItemListAmountTextWidth
            )

            if (it != allExpenseItem.size) {
                HorizontalLineOnSurface()
            }

        }

    }
}

// Private functions only for this file to reduce duplication
@Composable
private fun AddExpenseButton(
    modifier: Modifier = Modifier,
    appState: AppState,
    rightSideTitleHeight: MutableState<Dp>? = null,
    localDensity: Density? = null,
    orientation: Int = 0,
    visible : Boolean = true
) {
    if (orientation == 1 && visible) {
        GeneralButton(
            text = "ADD",
            modifier = modifier,
            enable = appState.drawerState?.value != true
        ) {
            appState.mainNavController.navigate(Screen.AddExpenseItemScreen.route)
        }
    } else if(visible) {
        GeneralButton(
            text = "ADD",
            modifier = Modifier
                .then(modifier)
                .onGloballyPositioned {
                    if (rightSideTitleHeight != null)
                        rightSideTitleHeight.value = with(localDensity!!) {
                            it.size.height.toDp()
                        }
                },
            enable = appState.drawerState?.value != true
        ) {
            appState.mainNavController.navigate(Screen.AddExpenseItemScreen.route)
        }
    }
}

@Composable
fun HorizontalLineOnBackground() {
    Spacer(
        Modifier
            .fillMaxWidth()
            .padding(vertical = halfGeneralPadding)
            .height(0.2.dp)
            .background(onBackground)
    )
}

@Composable
fun HorizontalLineOnSurface() {
    Spacer(
        Modifier
            .fillMaxWidth()
            .padding(vertical = halfGeneralPadding)
            .height(0.2.dp)
            .background(onBackground)
    )
}