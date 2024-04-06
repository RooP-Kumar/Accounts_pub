package com.zen.accounts.ui.screens.addexpense.addexpenseitem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.zen.accounts.R
import com.zen.accounts.db.model.ExpenseItem
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.component.GeneralButton
import com.zen.accounts.ui.component.GeneralEditText
import com.zen.accounts.ui.screens.addexpense.AddExpenseViewModel
import com.zen.accounts.ui.theme.add_item_button_label
import com.zen.accounts.ui.theme.background
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.halfGeneralPadding
import com.zen.accounts.ui.theme.shadowColor
import com.zen.accounts.utility.customShadow
import com.zen.accounts.utility.customShadowTwo
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


data class AddExpenseItemUiState(
    val type: MutableState<String> = mutableStateOf(""),
    val title: MutableState<String> = mutableStateOf(""),
    val quantity: MutableState<String> = mutableStateOf(""),
    val amount: MutableState<String> = mutableStateOf(""),
    val showDropDown: MutableState<Boolean> = mutableStateOf(false),
    val dropDownRowWidth: MutableState<Dp> = mutableStateOf(0.dp),
    val dropDownIcon: MutableState<Int> = mutableIntStateOf(R.drawable.ic_drop_down),
)

@Composable
fun AddExpenseItem(
    appState: AppState,
    viewModel: AddExpenseViewModel,
    paddingValues: PaddingValues
) {
    val uiState = viewModel.addExpenseItemUiState
    LaunchedEffect(key1 = uiState.showDropDown.value) {
        if (uiState.showDropDown.value) {
            uiState.dropDownIcon.value = R.drawable.ic_drop_up
        } else {
            uiState.dropDownIcon.value = R.drawable.ic_drop_down
        }
    }

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
    val localDensity = LocalDensity.current
    val allExpenseType = viewModel.allExpenseType.collectAsState(initial = listOf())
    val uiState = viewModel.addExpenseItemUiState
    val dropDownHeight : MutableState<Dp> = remember { mutableStateOf(0.dp) }
    val coroutineScope = rememberCoroutineScope()

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
                .customShadowTwo(radiusX = generalPadding, radiusY = generalPadding, blur = halfGeneralPadding)
                .clip(shape = RoundedCornerShape(generalPadding))
                .background(background)
                .padding(vertical = halfGeneralPadding)
        ) {

            Row {
                GeneralEditText(
                    text = uiState.type,
                    modifier = Modifier
                        .onGloballyPositioned {
                            uiState.dropDownRowWidth.value =
                                with(localDensity) { it.size.width.toDp() }
                        },
                    enable = false,
                    placeholderText = "Select Expense Type",
                    clickableFun = { uiState.showDropDown.value = !uiState.showDropDown.value },
                    trailingIcon = uiState.dropDownIcon.value,
                    trailingIconClick = { uiState.showDropDown.value = !uiState.showDropDown.value }
                )

                DropdownMenu(
                    expanded = uiState.showDropDown.value,
                    onDismissRequest = {
                        uiState.showDropDown.value = false
                    },
                    modifier = Modifier.size(width = 300.dp, height = dropDownHeight.value),
                    offset = DpOffset(uiState.dropDownRowWidth.value / 2 - 150.dp, (-4).dp)
                ) {
                    for (i in 0..<allExpenseType.value.size) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = allExpenseType.value[i].type,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            },
                            onClick = {
                                uiState.type.value = allExpenseType.value[i].type
                                uiState.showDropDown.value = false
                            },
                            modifier = Modifier
                                .onGloballyPositioned {
                                    dropDownHeight.value = with(localDensity) {it.size.height.toDp()} * if (allExpenseType.value.size < 6) allExpenseType.value.size else 5
                                }
                        )
                    }
                }
            }

            GeneralEditText(
                text = uiState.title,
                modifier = Modifier,
                placeholderText = "Title"
            )

            GeneralEditText(
                text = uiState.quantity,
                modifier = Modifier,
                placeholderText = "Quantity"
            )

            GeneralEditText(
                text = uiState.amount,
                modifier = Modifier,
                placeholderText = "Amount per Qty"
            )

            GeneralButton(
                text = add_item_button_label,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                val expenseItem = ExpenseItem(
                    itemName = uiState.title.value.trim(),
                    itemType = uiState.type.value.trim(),
                    itemPrice = uiState.amount.value.trim().toLong(),
                    itemQty = uiState.quantity.value.trim().toInt()
                )
                viewModel.addExpenseItemIntoLocalDatabase(expenseItem)
                coroutineScope.launch {
                    delay(2000L)
                    appState.mainNavController.popBackStack()
                }
            }

        }
    }

}