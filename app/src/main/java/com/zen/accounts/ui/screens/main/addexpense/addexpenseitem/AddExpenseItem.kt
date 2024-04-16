package com.zen.accounts.ui.screens.main.addexpense.addexpenseitem

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zen.accounts.R
import com.zen.accounts.db.model.AmountType
import com.zen.accounts.db.model.ExpenseItem
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.screens.common.GeneralButton
import com.zen.accounts.ui.screens.common.GeneralDropDown
import com.zen.accounts.ui.screens.common.GeneralEditText
import com.zen.accounts.ui.screens.common.LoadingDialog
import com.zen.accounts.ui.screens.main.addexpense.AddExpenseViewModel
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.add_item_button_label
import com.zen.accounts.ui.theme.background
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.halfGeneralPadding
import com.zen.accounts.ui.theme.onSurface
import com.zen.accounts.utility.customShadowTwo
import com.zen.accounts.utility.enums.LoadingState
import com.zen.accounts.utility.toast
import kotlinx.coroutines.launch


data class AddExpenseItemUiState(
    val type: MutableState<String> = mutableStateOf(""),
    val amountType: MutableState<String> = mutableStateOf("A/Q"),
    val title: MutableState<String> = mutableStateOf(""),
    val from: MutableState<String> = mutableStateOf(""),
    val destination: MutableState<String> = mutableStateOf(""),
    val quantity: MutableState<String> = mutableStateOf(""),
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
    viewModel: AddExpenseViewModel,
    paddingValues: PaddingValues
) {
    val uiState = viewModel.addExpenseItemUiState
    DisposableEffect(key1 = uiState.showDropDown.value) {
        if (uiState.showDropDown.value) {
            uiState.dropDownIcon.value = R.drawable.ic_drop_up
        } else {
            uiState.dropDownIcon.value = R.drawable.ic_drop_down
        }
        onDispose { uiState.loadingState.value = LoadingState.IDLE }
    }

    LoadingDialog(loadingState = uiState.loadingState)

    MainUI(
        appState,
        viewModel,
        paddingValues
    )
}

@Composable
private fun MainUI(
    appState: AppState,
    viewModel: AddExpenseViewModel,
    paddingValues: PaddingValues
) {
    val localContext = LocalContext.current
    val allExpenseType = viewModel.allExpenseType.collectAsState(initial = listOf())
    val uiState = viewModel.addExpenseItemUiState
    val coroutineScope = rememberCoroutineScope()
    val amountPlaceholder = if (uiState.amountType.value == "A/Q") "Amount per Qty" else "Total Amount"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(generalPadding)
            .padding(top = paddingValues.calculateTopPadding())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .customShadowTwo(
                    radiusX = generalPadding,
                    radiusY = generalPadding,
                    blur = halfGeneralPadding
                )
                .clip(shape = RoundedCornerShape(generalPadding))
                .background(background)
                .padding(vertical = halfGeneralPadding)
        ) {

            GeneralDropDown(
                modifier = Modifier,
                value = uiState.type,
                showDropDown = uiState.showDropDown,
                valueList = allExpenseType.value
            )

            AnimatedVisibility(visible = uiState.type.value != "Travel") {
                Column {
                    GeneralEditText(
                        text = uiState.title,
                        modifier = Modifier.fillMaxWidth(),
                        placeholderText = "Title",
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )

                    GeneralEditText(
                        text = uiState.quantity,
                        modifier = Modifier.fillMaxWidth(),
                        placeholderText = "Quantity",
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next
                        )
                    )
                }
            }

            AnimatedVisibility(visible = uiState.type.value == "Travel") {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GeneralEditText(
                        text = uiState.from,
                        modifier = Modifier.width(175.dp),
                        placeholderText = "From",
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )

                    Text(
                        text = "To",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = Typography.bodyMedium.copy(color = onSurface)
                    )

                    GeneralEditText(
                        text = uiState.destination,
                        modifier = Modifier.width(175.dp),
                        placeholderText = "Destination",
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )
                }

            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GeneralEditText(
                    text = uiState.amount,
                    modifier = Modifier.weight(2f),
                    placeholderText = if (uiState.type.value != "Travel") amountPlaceholder else "Ticket Amount",
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
                )

                GeneralDropDown(
                    modifier = Modifier.weight(1f),
                    value = uiState.amountType,
                    showDropDown = uiState.showAmountDropDown,
                    valueList = listOf(
                        AmountType(3, "A/Q"),
                        AmountType(4, "TA"),
                    )
                )

            }

            GeneralButton(
                text = add_item_button_label,
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                val itemTitle = uiState.title.value.trim()
                val itemType = uiState.type.value.trim()
                val itemPrice = uiState.amount.value.trim()
                val itemQty = uiState.quantity.value.trim()
                val itemFrom = uiState.from.value.trim()
                val itemDestination = uiState.destination.value.trim()
                var shouldRun = true

                if (itemType.isEmpty()) {
                    localContext.toast("Please select expense type")
                    shouldRun = false
                } else if(itemTitle.isEmpty() && itemType != "Travel") {
                    localContext.toast("Please enter title.")
                    shouldRun = false
                } else if (itemQty.isEmpty() && itemType != "Travel") {
                    localContext.toast("Quantity can not be empty")
                    shouldRun = false
                } else if (itemPrice.isEmpty()) {
                    localContext.toast("Amount can not be empty.")
                    shouldRun = false
                } else if(itemType == "Travel"){
                    if(itemFrom.isEmpty()) {
                        localContext.toast("Please enter source.")
                        shouldRun = false
                    }
                    if (itemDestination.isEmpty()) {
                        localContext.toast("Please enter destination.")
                        shouldRun = false
                    }
                }

                if(shouldRun) {
                    val expenseItem = ExpenseItem(
                        id = System.currentTimeMillis(),
                        itemName = itemTitle.ifEmpty { "Traveled from $itemFrom to $itemDestination" },
                        itemType = itemType,
                        itemPrice = itemPrice.toLong(),
                        itemQty = (itemQty.ifEmpty { 0 }).toString().toInt(),
                        itemAmountType = uiState.amountType.value,
                    )
                    viewModel.addExpenseItemIntoLocalDatabase(expenseItem)
                    coroutineScope.launch {
                        appState.mainNavController.popBackStack()
                    }
                }
            }

        }
    }

}