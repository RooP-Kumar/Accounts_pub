package com.zen.accounts.ui.screens.main.add_expense_item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zen.accounts.R
import com.zen.accounts.db.model.ExpenseItem
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.screens.common.GeneralButton
import com.zen.accounts.ui.screens.common.GeneralEditText
import com.zen.accounts.ui.screens.common.LoadingDialog
import com.zen.accounts.ui.screens.common.LoadingState
import com.zen.accounts.ui.screens.common.TopBarBackButton
import com.zen.accounts.ui.screens.common.add_expense_item_screen_label
import com.zen.accounts.ui.screens.common.add_item_button_label
import com.zen.accounts.ui.screens.common.getQuantityAmountRelation
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.background
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.halfGeneralPadding
import com.zen.accounts.ui.theme.onBackground
import com.zen.accounts.ui.viewmodels.AddExpenseViewModel
import com.zen.accounts.utility.toast
import kotlinx.coroutines.launch


data class AddExpenseItemUiState(
    val title: MutableState<String> = mutableStateOf(""),
    val amount: MutableState<String> = mutableStateOf(""),
    val showDropDown: MutableState<Boolean> = mutableStateOf(false),
    val showAmountDropDown: MutableState<Boolean> = mutableStateOf(false),
    val dropDownRowWidth: MutableState<Dp> = mutableStateOf(0.dp),
    val dropDownIcon: MutableState<Int> = mutableIntStateOf(R.drawable.ic_drop_down),
    val loadingState : MutableState<LoadingState> = mutableStateOf(LoadingState.IDLE)
)

@Composable
fun AddExpenseItem(
    appState: AppState,
    viewModel: AddExpenseViewModel
) {
    val uiState = viewModel.addExpenseItemUiState
    val coroutineScope = rememberCoroutineScope()
    val allAmountType = getQuantityAmountRelation()

    DisposableEffect(key1 = uiState.showDropDown.value) {
        if (uiState.showDropDown.value) {
            uiState.dropDownIcon.value = R.drawable.ic_drop_up
        } else {
            uiState.dropDownIcon.value = R.drawable.ic_drop_down
        }
        onDispose { uiState.loadingState.value = LoadingState.IDLE }
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TopBarBackButton(appState = appState)

            Text(
                text = add_expense_item_screen_label,
                style = Typography.bodyLarge.copy(onBackground),
                modifier = Modifier
                    .padding(generalPadding)
                    .weight(1f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(background)
                .padding(vertical = generalPadding)
        ) {

            GeneralEditText(
                text = uiState.title.value,
                onValueChange = {uiState.title.value = it},
                modifier = Modifier.fillMaxWidth(),
                placeholderText = "Title",
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
            GeneralEditText(
                text = uiState.amount.value,
                onValueChange = {uiState.amount.value = it},
                modifier = Modifier.fillMaxWidth(),
                placeholderText = "Amount",
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )

            GeneralButton(
                text = add_item_button_label,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = generalPadding)
                    .padding(top = halfGeneralPadding)
            ) {

                val itemTitle = uiState.title.value.trim()
                val itemPrice = uiState.amount.value.trim()
                var shouldRun = true

                if(itemTitle.isEmpty()) {
                    localContext.toast("Please enter title.")
                    shouldRun = false
                } else if (itemPrice.isEmpty()) {
                    localContext.toast("Amount can not be empty.")
                    shouldRun = false
                }

                if(shouldRun) {
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