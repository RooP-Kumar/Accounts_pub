package com.zen.accounts.ui.screens.main.add_expense_item

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zen.accounts.R
import com.zen.accounts.db.model.ExpenseItem
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.screens.common.GeneralButton
import com.zen.accounts.ui.screens.common.GeneralDialog
import com.zen.accounts.ui.screens.common.GeneralEditText
import com.zen.accounts.ui.screens.common.GeneralSnackBar
import com.zen.accounts.ui.screens.common.LoadingDialog
import com.zen.accounts.ui.screens.common.LoadingState
import com.zen.accounts.ui.screens.common.TopAppBar
import com.zen.accounts.ui.screens.common.TopBarBackButton
import com.zen.accounts.ui.screens.common.add_expense_item_screen_label
import com.zen.accounts.ui.screens.common.add_item_button_label
import com.zen.accounts.ui.screens.common.getQuantityAmountRelation
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.background
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.halfGeneralPadding
import com.zen.accounts.ui.theme.onBackground
import com.zen.accounts.ui.theme.onSurface
import com.zen.accounts.ui.theme.red_color
import com.zen.accounts.ui.theme.surface
import com.zen.accounts.ui.theme.topBarHeight
import com.zen.accounts.ui.viewmodels.AddExpenseViewModel
import com.zen.accounts.utility.Utility
import com.zen.accounts.utility.toast
import kotlinx.coroutines.launch


data class AddExpenseItemUiState(
    val title: MutableState<String> = mutableStateOf(""),
    val amount: MutableState<String> = mutableStateOf(""),
    val showAmountDropDown: MutableState<Boolean> = mutableStateOf(false),
    val dropDownRowWidth: MutableState<Dp> = mutableStateOf(0.dp),
    val dropDownIcon: MutableState<Int> = mutableIntStateOf(R.drawable.ic_drop_down),
    val loadingState : MutableState<LoadingState> = mutableStateOf(LoadingState.IDLE),
    val snackBarState: MutableState<Boolean> = mutableStateOf(false),
    val snackBarText : MutableState<String> = mutableStateOf(""),
    val confirmationDialogState : MutableState<Boolean> = mutableStateOf(false),
)

@Composable
fun AddExpenseItem(
    appState: AppState,
    viewModel: AddExpenseViewModel
) {
    val uiState = viewModel.addExpenseItemUiState
    val coroutineScope = rememberCoroutineScope()

    BackHandler(enabled = uiState.title.value.isNotEmpty() || uiState.amount.value.isNotEmpty()) {
        uiState.confirmationDialogState.value = true
    }

    LoadingDialog(
        loadingState = uiState.loadingState,
        onSuccess = {
            coroutineScope.launch {
                appState.navController.popBackStack()
            }
        }
    )

    MainUI(
        appState,
        viewModel
    )
}

@Composable
private fun MainUI(
    appState: AppState,
    viewModel: AddExpenseViewModel
) {
    val localContext = LocalContext.current
    val uiState = viewModel.addExpenseItemUiState

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {}
            .background(background)
    ) {
        TopAppBar(appState = appState)
        
        GeneralSnackBar(
            visible = uiState.snackBarState,
            text = uiState.snackBarText.value,
            containerColor = red_color
        )

        GeneralDialog(showDialog = uiState.confirmationDialogState) {
            Column(
                modifier = Modifier
                    .background(surface)
                    .padding(generalPadding)
            ) {
                Text(
                    text = "Are you sure?\nYou want to go back, Your data will be lost.",
                    textAlign = TextAlign.Center,
                    style = Typography.bodyLarge.copy(onSurface),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(generalPadding))
                Row {
                    GeneralButton(
                        text = "NO",
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        uiState.confirmationDialogState.value = false
                    }
                    
                    Spacer(modifier = Modifier.width(generalPadding))

                    GeneralButton(
                        text = "YES",
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        appState.navController.popBackStack()
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topBarHeight)
                .background(background)
                .padding(vertical = generalPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            GeneralEditText(
                text = uiState.title.value,
                onValueChange = {uiState.title.value = it},
                modifier = Modifier.width(400.dp),
                placeholderText = "Title",
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
            GeneralEditText(
                text = uiState.amount.value,
                onValueChange = {uiState.amount.value = it},
                modifier = Modifier.width(400.dp),
                placeholderText = "Amount",
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )

            GeneralButton(
                text = add_item_button_label,
                modifier = Modifier
                    .width(400.dp)
                    .padding(horizontal = generalPadding)
                    .padding(top = halfGeneralPadding)
            ) {

                val itemTitle = uiState.title.value.trim()
                val itemPrice = uiState.amount.value.trim()

                if(itemTitle.isEmpty()) {
                    uiState.snackBarText.value = "Please enter title."
                    Utility.showSnackBar(uiState.snackBarState)
                } else if (itemPrice.isEmpty()) {
                    uiState.snackBarText.value = "Amount can not be empty."
                    Utility.showSnackBar(uiState.snackBarState)
                } else {
                    val expenseItem = ExpenseItem(
                        id = System.currentTimeMillis(),
                        itemTitle = itemTitle,
                        itemAmount = itemPrice.toDouble()
                    )
                    viewModel.addExpenseItemIntoLocalDatabase(expenseItem)
                }
            }

        }
    }

}