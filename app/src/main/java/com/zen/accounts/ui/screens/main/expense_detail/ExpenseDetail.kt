package com.zen.accounts.ui.screens.main.expense_detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zen.accounts.R
import com.zen.accounts.db.model.Expense
import com.zen.accounts.db.model.ExpenseItem
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.screens.common.GeneralButton
import com.zen.accounts.ui.screens.common.GeneralDialog
import com.zen.accounts.ui.screens.common.GeneralEditText
import com.zen.accounts.ui.screens.common.TopAppBar
import com.zen.accounts.ui.screens.common.enter_amount
import com.zen.accounts.ui.screens.common.enter_title
import com.zen.accounts.ui.screens.common.getRupeeString
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.background
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.green_color
import com.zen.accounts.ui.theme.halfGeneralPadding
import com.zen.accounts.ui.theme.onBackground
import com.zen.accounts.ui.theme.red_color
import com.zen.accounts.ui.theme.secondary_color
import com.zen.accounts.ui.viewmodels.ExpenseDetailsViewModel
import com.zen.accounts.utility.generalBorder
import com.zen.accounts.utility.main
import com.zen.accounts.utility.toast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ExpenseDetailUIState(
    val expense : MutableState<Expense> = mutableStateOf(Expense()),
    val expenseItems : SnapshotStateList<ExpenseItem> = mutableStateListOf(),
    val showEditDialog : MutableState<Boolean> = mutableStateOf(false),
    val showDeleteDialog : MutableState<Boolean> = mutableStateOf(false),
)

@Composable
fun ExpenseDetails(
    appState : AppState,
    expense: Expense,
    expenseDetailsViewModel : ExpenseDetailsViewModel
) {
    val uiState = expenseDetailsViewModel.expenseDetailsUiState
    LaunchedEffect(key1 = Unit) {
        if(uiState.expense.value.id == 0L) {
           uiState.expense.value = expense
        }
    }
    LaunchedEffect(key1= Unit) {
        if(uiState.expenseItems.isEmpty()) {
            uiState.expenseItems.addAll(expense.items)
        }
    }

    MainUI(
        appState,
        expenseDetailsViewModel
    )
}

@Composable
private fun MainUI(
    appState: AppState,
    expenseDetailsViewModel: ExpenseDetailsViewModel
) {
    val uiState = expenseDetailsViewModel.expenseDetailsUiState
    val coroutineScope = rememberCoroutineScope()
    val expendedItemInd = remember { mutableIntStateOf(-1) }
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val isExpenseTitle = remember { mutableStateOf(false) }

    ExpenseItemEditDialog(
        showDialog = uiState.showEditDialog,
        isExpenseTitle = isExpenseTitle.value,
        getExpenseTitle = { uiState.expense.value.title },
        expenseItem = if(expendedItemInd.intValue == -1) ExpenseItem()  else uiState.expense.value.items[expendedItemInd.intValue]
    ) {updatedExpenseItem, updatedExpenseTitle->
        uiState.expense.value.title = updatedExpenseTitle ?: uiState.expense.value.title
        expenseDetailsViewModel.updateExpense(
            updatedExpenseItem,
            expendedItemInd.intValue
        )
        expendedItemInd.intValue = -1
    }

    ExpenseItemDeleteDialog(
        showDialog = uiState.showDeleteDialog,
        onYes = {
            if(uiState.expenseItems.size == 1) {
                expenseDetailsViewModel.deleteExpense()
                main {
                    delay(300)
                    appState.navController.popBackStack()
                }
            } else {
                expenseDetailsViewModel.deleteExpenseItem(expendedItemInd.intValue)
                expendedItemInd.intValue = -1
                uiState.showDeleteDialog.value = false
            }
        },
        onNo = { uiState.showDeleteDialog.value = false }
    )


    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {}
            .background(background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                appState = appState,
                painterResource = painterResource(id = R.drawable.ic_edit_pencil)
            ) {
                isExpenseTitle.value = true
                uiState.showEditDialog.value = true
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = uiState.expense.value.title,
                    textAlign = TextAlign.Center,
                    style = Typography.bodyLarge.copy(color = onBackground),
                    modifier = Modifier
                        .padding(horizontal = generalPadding)
                )

                Spacer(modifier = Modifier.height(halfGeneralPadding))

                Text(
                    text = getRupeeString(uiState.expense.value.totalAmount),
                    textAlign = TextAlign.Center,
                    style = Typography.bodyLarge.copy(color = onBackground),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(halfGeneralPadding))
            val listState = rememberLazyListState()
            val tempModifier = Modifier
                .fillMaxSize()
                .padding(generalPadding)
                .generalBorder()
                .padding(end = generalPadding)

            if(screenWidth <= 500) {
                LazyColumn(
                    modifier = tempModifier
                    ,
                    state = listState
                ) {
                    items(uiState.expenseItems.size, key = { uiState.expenseItems[it].id }) { expenseItemInd ->
                        ExpenseItemListLayout(
                            expenseItem = uiState.expenseItems[expenseItemInd],
                            expendedItemInd.intValue == expenseItemInd,
                            uiState.showEditDialog,
                            uiState.showDeleteDialog,
                            uiState.expenseItems.size-1 == expenseItemInd
                        ) {
                            coroutineScope.launch {
                                expendedItemInd.intValue = if(expendedItemInd.intValue == expenseItemInd){
                                    -1
                                } else {
                                    expenseItemInd
                                }
                            }
                        }
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = tempModifier
                ) {
                    items(uiState.expenseItems.size, key = { uiState.expenseItems[it].id }) { expenseItemInd ->
                        ExpenseItemListLayout(
                            expenseItem = uiState.expenseItems[expenseItemInd],
                            expendedItemInd.intValue == expenseItemInd,
                            uiState.showEditDialog,
                            uiState.showDeleteDialog,
                            uiState.expenseItems.size-2 == expenseItemInd || uiState.expenseItems.size-1 == expenseItemInd
                        ) {
                            isExpenseTitle.value = false
                            coroutineScope.launch {
                                expendedItemInd.intValue = if(expendedItemInd.intValue == expenseItemInd){
                                    -1
                                } else {
                                    expenseItemInd
                                }
                            }
                        }
                    }
                }
            }


        }
    }
}

@Composable
fun ExpenseItemListLayout(
    expenseItem: ExpenseItem,
    expend : Boolean,
    showEditDialog: MutableState<Boolean>,
    showDeleteDialog: MutableState<Boolean>,
    isLast : Boolean,
    onItemClick : () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = generalPadding,
                start = generalPadding,
                bottom = if (isLast) generalPadding else 0.dp
            )
            .generalBorder()
            .clickable {
                onItemClick()
            }
            .background(secondary_color),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(generalPadding),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = expenseItem.itemTitle,
                style = Typography.bodyMedium.copy(color = onBackground)
            )
            Text(
                text = getRupeeString(expenseItem.itemAmount ?: 0.0),
                style = Typography.bodyMedium.copy(color = onBackground)
            )
        }

        AnimatedVisibility(visible = expend) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(generalPadding)
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .generalBorder()
                            .clickable {
                                showEditDialog.value = true
                            }
                            .background(green_color)
                            .padding(generalPadding),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            painterResource(id = R.drawable.ic_edit),
                            contentDescription = "editbutton",
                            tint = background,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(generalPadding))
                        Text(
                            text = "EDIT",
                            style = Typography.bodyMedium.copy(color = background)
                        )
                    }

                    Spacer(modifier = Modifier.width(generalPadding))
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .generalBorder()
                            .clickable {
                                showDeleteDialog.value = true
                            }
                            .background(red_color)
                            .padding(generalPadding),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            painterResource(id = R.drawable.ic_bin),
                            contentDescription = "delete buttom",
                            tint = background,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(generalPadding))
                        Text(
                            text = "DELETE",
                            style = Typography.bodyMedium.copy(color = background)
                        )
                    }
                }
            }

    }
}

@Composable
fun ExpenseItemEditDialog(
    showDialog : MutableState<Boolean>,
    expenseItem: ExpenseItem,
    isExpenseTitle : Boolean = false,
    getExpenseTitle : () -> String,
    onSave : (ExpenseItem?, String?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    val context = LocalContext.current

    title = expenseItem.itemTitle
    amount = expenseItem.itemAmount.toString()

    if(isExpenseTitle) {
        title = getExpenseTitle()
    }

    GeneralDialog(
        showDialog = showDialog
    ) {
        GeneralEditText(
            text = title,
            onValueChange = {
                title = it
            },
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            placeholderText = enter_title
        )
        if(!isExpenseTitle){
            GeneralEditText(
                text = amount,
                onValueChange = {
                    amount = it
                },
                modifier = Modifier
                    .fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Decimal
                ),
                placeholderText = enter_amount
            )
        }
        GeneralButton(
            text = "SAVE"
        ) {
            if(amount.trim().isEmpty()) {
                context.toast("Amount can not be empty.")
            } else {
                showDialog.value = false
                if(isExpenseTitle) {
                    onSave(null, title.trim())
                } else {
                    onSave(expenseItem.copy(itemTitle = title.trim(), itemAmount = amount.trim().toDouble()), null)
                }
            }
        }
        Spacer(modifier = Modifier.height(halfGeneralPadding))
    }
}

@Composable
fun ExpenseItemDeleteDialog(
    showDialog: MutableState<Boolean>,
    onYes : () -> Unit,
    onNo : () -> Unit
) {
    GeneralDialog(showDialog = showDialog) {
        Text(
            text = "You want to delete this item.\n Are you Sure?",
            textAlign = TextAlign.Center,
            style = Typography.bodyMedium.copy(color = onBackground),
            modifier = Modifier
                .fillMaxWidth()
                .padding(generalPadding)
        )

        Spacer(modifier = Modifier.height(generalPadding))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(generalPadding)
        ) {
            GeneralButton(
                text = "NO",
                modifier = Modifier.weight(1f)
            ) {
                onNo()
            }

            Spacer(modifier = Modifier.width(generalPadding))

            GeneralButton(
                text = "YES",
                modifier = Modifier.weight(1f)
            ) {
                onYes()
            }
        }
    }
}