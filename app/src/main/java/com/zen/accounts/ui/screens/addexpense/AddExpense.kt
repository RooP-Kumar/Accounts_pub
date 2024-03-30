package com.zen.accounts.ui.screens.addexpense

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.zen.accounts.R
import com.zen.accounts.db.model.ExpenseType
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.component.GeneralButton
import com.zen.accounts.ui.component.GeneralEditText
import com.zen.accounts.ui.theme.add_item_button_label
import com.zen.accounts.ui.theme.background
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.halfGeneralPadding
import com.zen.accounts.ui.theme.normalPadding
import com.zen.accounts.ui.theme.shadowColor

data class AddExpenseUiState(
    val type: MutableState<String> = mutableStateOf(""),
    val title: MutableState<String> = mutableStateOf(""),
    val amount: MutableState<String> = mutableStateOf(""),
    val showDropDown: MutableState<Boolean> = mutableStateOf(false),
    val dropDownRowWidth: MutableState<Dp> = mutableStateOf(0.dp),
    val dropDownIcon: MutableState<Int> = mutableIntStateOf(R.drawable.ic_drop_down),
)

@Composable
fun AddExpense(
    appState: AppState,
    viewModel: AddExpenseViewModel
) {
    val uiState = viewModel.addExpenseUiState
    LaunchedEffect(key1 = uiState.showDropDown.value) {
        if (uiState.showDropDown.value) {
            uiState.dropDownIcon.value = R.drawable.ic_drop_up
        } else {
            uiState.dropDownIcon.value = R.drawable.ic_drop_down
        }
    }
    MainUi(viewModel)
}

@Composable
private fun MainUi(viewModel: AddExpenseViewModel) {
    val localDensity = LocalDensity.current
    val uiState = viewModel.addExpenseUiState
    val allExpenseType = viewModel.allExpenseType.collectAsState(initial = listOf())
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = generalPadding)
                .shadow(
                    elevation = halfGeneralPadding,
                    shape = RoundedCornerShape(generalPadding),
                    ambientColor = shadowColor,
                    spotColor = shadowColor
                )
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
                    modifier = Modifier.size(width = 300.dp, height = 250.dp),
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
                            }
                        )
                    }
                }
            }

            GeneralEditText(
                text = uiState.title,
                modifier = Modifier,
                placeholderText = "Enter title"
            )

            GeneralEditText(
                text = uiState.amount,
                modifier = Modifier,
                placeholderText = "Enter amount"
            )

            GeneralButton(
                text = add_item_button_label,
                modifier = Modifier
            ) {

            }

        }
    }
}