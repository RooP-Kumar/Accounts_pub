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
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zen.accounts.R
import com.zen.accounts.db.model.Expense
import com.zen.accounts.db.model.ExpenseItem
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.navigation.Screen
import com.zen.accounts.ui.screens.common.GeneralButton
import com.zen.accounts.ui.screens.common.GeneralDialog
import com.zen.accounts.ui.screens.common.GeneralEditText
import com.zen.accounts.ui.screens.common.TopAppBar
import com.zen.accounts.ui.screens.common.enter_amount
import com.zen.accounts.ui.screens.common.enter_title
import com.zen.accounts.ui.screens.common.getRupeeString
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.green_color
import com.zen.accounts.ui.theme.halfGeneralPadding
import com.zen.accounts.ui.theme.red_color
import com.zen.accounts.ui.theme.secondary_color
import com.zen.accounts.ui.viewmodels.ExpenseDetailsViewModel
import com.zen.accounts.utility.generalBorder
import com.zen.accounts.utility.main
import com.zen.accounts.utility.toast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ExpenseDetailUIState(
    var expense : Expense = Expense(),
    var expenseItems : ArrayList<ExpenseItem> = arrayListOf(),
    var showEditDialog : Boolean = false,
    var showDeleteDialog : Boolean = false,
)

const val ExpenseDetailUIStateExpense = "expense"
const val ExpenseDetailUIStateExpenseItems = "expenseItems"
const val ExpenseDetailUIStateShowEditDialog = "showEditDialog"
const val ExpenseDetailUIStateShowDeleteDialog = "showDeleteDialog"

@Composable
fun ExpenseDetails(
    drawerState : MutableState<DrawerState?>,
    expense: Expense,
    expenseDetailsViewModel : ExpenseDetailsViewModel,
    navigateUp: () -> Boolean,
    currentScreen: Screen?
) {
    val expenseDetailsUiState = expenseDetailsViewModel.expenseDetailsUiStateHolder.collectAsState()
    LaunchedEffect(key1 = Unit) {
        if(expenseDetailsUiState.value.expense.id == 0L) {
           expenseDetailsViewModel.updateExpenseDetailsUiState(expense, ExpenseDetailUIStateExpense)
        }
    }
    LaunchedEffect(key1= Unit) {
        if(expenseDetailsUiState.value.expenseItems.isEmpty()) {
            val tempExpenseHolder = expenseDetailsUiState.value.copy()
            tempExpenseHolder.expenseItems.addAll(expense.items)
            expenseDetailsViewModel.updateExpenseDetailsUiState(tempExpenseHolder.expenseItems, ExpenseDetailUIStateExpenseItems)
        }
    }

    MainUI(
        drawerState = drawerState,
        expenseDetailsUiState.value,
        navigateUp = navigateUp,
        expenseDetailsViewModel::updateExpenseDetailsUiState,
        updateExpense = expenseDetailsViewModel::updateExpense,
        deleteExpense = expenseDetailsViewModel::deleteExpense,
        deleteExpenseItem = expenseDetailsViewModel::deleteExpenseItem,
        currentScreen
    )
}

@Composable
private fun MainUI(
    drawerState : MutableState<DrawerState?>,
    expenseDetailsUiState: ExpenseDetailUIState,
    navigateUp : () -> Boolean,
    updateExpenseDetailsUiState: (Any, String) -> Unit,
    updateExpense : (ExpenseItem?, Int) -> Unit,
    deleteExpense : () -> Unit,
    deleteExpenseItem : (Int) -> Unit,
    currentScreen: Screen?
) {
    
    val coroutineScope = rememberCoroutineScope()
    val expendedItemInd = remember { mutableIntStateOf(-1) }
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val isExpenseTitle = remember { mutableStateOf(false) }

    ExpenseItemEditDialog(
        showDialog = expenseDetailsUiState.showEditDialog,
        isExpenseTitle = isExpenseTitle.value,
        getExpenseTitle = { expenseDetailsUiState.expense.title },
        expenseItem = if(expendedItemInd.intValue == -1) ExpenseItem()  else expenseDetailsUiState.expense.items[expendedItemInd.intValue],
        onEditDialogSaveClick = {updatedExpenseTitle, updatedExpenseItem ->
            updateExpenseDetailsUiState(expenseDetailsUiState.expense.copy(title = updatedExpenseTitle ?: ""), ExpenseDetailUIStateExpense)
            updateExpense(
                updatedExpenseItem,
                expendedItemInd.intValue
            )
            expendedItemInd.intValue = -1
        },
        updateAddExpenseStateValue = updateExpenseDetailsUiState
    )

    ExpenseItemDeleteDialog(
        showDialog = expenseDetailsUiState.showDeleteDialog,
        onYes = {
            if(expenseDetailsUiState.expenseItems.size == 1) {
                deleteExpense()
                main {
                    delay(300)
                    navigateUp()
                }
            } else {
                deleteExpenseItem(expendedItemInd.intValue)
                expendedItemInd.intValue = -1
                updateExpenseDetailsUiState(false, ExpenseDetailUIStateShowDeleteDialog)
            }
        },
        onNo = { updateExpenseDetailsUiState(false, ExpenseDetailUIStateShowDeleteDialog) }
    )


    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {}
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                drawerState = drawerState,
                navigateUp = navigateUp,
                painterResource = painterResource(id = R.drawable.ic_edit_pencil),
                currentScreen = currentScreen
            ) {
                isExpenseTitle.value = true
                updateExpenseDetailsUiState(true, ExpenseDetailUIStateShowDeleteDialog)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = expenseDetailsUiState.expense.title,
                    textAlign = TextAlign.Center,
                    style = Typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier
                        .padding(horizontal = generalPadding)
                )

                Spacer(modifier = Modifier.height(halfGeneralPadding))

                Text(
                    text = getRupeeString(expenseDetailsUiState.expense.totalAmount),
                    textAlign = TextAlign.Center,
                    style = Typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
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
                    items(expenseDetailsUiState.expenseItems.size, key = { expenseDetailsUiState.expenseItems[it].id }) { expenseItemInd ->
                        ExpenseItemListLayout(
                            expenseItem = expenseDetailsUiState.expenseItems[expenseItemInd],
                            expendedItemInd.intValue == expenseItemInd,
                            onEditClick = {updateExpenseDetailsUiState(it, ExpenseDetailUIStateShowEditDialog)},
                            onDeleteClick = {updateExpenseDetailsUiState(it, ExpenseDetailUIStateShowDeleteDialog)},
                            isLast = expenseDetailsUiState.expenseItems.size-1 == expenseItemInd
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
                    items(expenseDetailsUiState.expenseItems.size, key = { expenseDetailsUiState.expenseItems[it].id }) { expenseItemInd ->
                        ExpenseItemListLayout(
                            expenseItem = expenseDetailsUiState.expenseItems[expenseItemInd],
                            expendedItemInd.intValue == expenseItemInd,
                            onEditClick = {updateExpenseDetailsUiState(it, ExpenseDetailUIStateShowEditDialog)},
                            onDeleteClick = {updateExpenseDetailsUiState(it, ExpenseDetailUIStateShowDeleteDialog)},
                            isLast = expenseDetailsUiState.expenseItems.size-2 == expenseItemInd || expenseDetailsUiState.expenseItems.size-1 == expenseItemInd
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
    onDeleteClick : (Boolean) -> Unit,
    onEditClick : (Boolean) -> Unit,
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
                style = Typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground)
            )
            Text(
                text = getRupeeString(expenseItem.itemAmount ?: 0.0),
                style = Typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground)
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
                                onEditClick(true)
                            }
                            .background(green_color)
                            .padding(generalPadding),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            painterResource(id = R.drawable.ic_edit),
                            contentDescription = "editbutton",
                            tint = MaterialTheme.colorScheme.background,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(generalPadding))
                        Text(
                            text = "EDIT",
                            style = Typography.bodyMedium.copy(color = MaterialTheme.colorScheme.background)
                        )
                    }

                    Spacer(modifier = Modifier.width(generalPadding))
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .generalBorder()
                            .clickable {
                                onDeleteClick(true)
                            }
                            .background(red_color)
                            .padding(generalPadding),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            painterResource(id = R.drawable.ic_bin),
                            contentDescription = "delete buttom",
                            tint = MaterialTheme.colorScheme.background,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(generalPadding))
                        Text(
                            text = "DELETE",
                            style = Typography.bodyMedium.copy(color = MaterialTheme.colorScheme.background)
                        )
                    }
                }
            }

    }
}

@Composable
fun ExpenseItemEditDialog(
    showDialog : Boolean,
    expenseItem: ExpenseItem,
    isExpenseTitle : Boolean = false,
    getExpenseTitle : () -> String,
    updateAddExpenseStateValue : (Any, String) -> Unit,
    onEditDialogSaveClick: (String?, ExpenseItem?) -> Unit
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
            } else if (title.trim().isEmpty()) {
                context.toast("Please! Enter Title.")
            } else {
                updateAddExpenseStateValue(false, ExpenseDetailUIStateShowEditDialog)
                if(isExpenseTitle) {
                    onEditDialogSaveClick(title.trim(), null)
                } else {
                    try {
                        onEditDialogSaveClick(
                            null,
                            expenseItem.copy(
                                itemTitle = title.trim(),
                                itemAmount = amount.trim().toDouble()
                            )
                        )
                    } catch (e: Exception) {
                        context.toast("Amount should only contains numbers.")
                    }
                }
            }

        }
        Spacer(modifier = Modifier.height(halfGeneralPadding))
    }
}

@Composable
fun ExpenseItemDeleteDialog(
    showDialog: Boolean,
    onYes : () -> Unit,
    onNo : () -> Unit
) {
    GeneralDialog(showDialog = showDialog) {
        Text(
            text = "You want to delete this item.\n Are you Sure?",
            textAlign = TextAlign.Center,
            style = Typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground),
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