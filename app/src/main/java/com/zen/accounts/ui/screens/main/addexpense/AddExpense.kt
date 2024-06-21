package com.zen.accounts.ui.screens.main.addexpense

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.zen.accounts.R
import com.zen.accounts.db.model.Expense
import com.zen.accounts.db.model.ExpenseItem
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.navigation.Screen
import com.zen.accounts.ui.screens.common.GeneralButton
import com.zen.accounts.ui.screens.common.GeneralSnackBar
import com.zen.accounts.ui.screens.common.LoadingDialog
import com.zen.accounts.ui.screens.common.LoadingState
import com.zen.accounts.ui.screens.common.TopAppBar
import com.zen.accounts.ui.screens.common.getRupeeString
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.background
import com.zen.accounts.ui.theme.border_color
import com.zen.accounts.ui.theme.enabled_color
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.halfGeneralPadding
import com.zen.accounts.ui.theme.onBackground
import com.zen.accounts.ui.theme.onSurface
import com.zen.accounts.ui.theme.red_color
import com.zen.accounts.ui.theme.text_color
import com.zen.accounts.ui.theme.topBarHeight
import com.zen.accounts.ui.viewmodels.AddExpenseViewModel
import com.zen.accounts.utility.Utility
import com.zen.accounts.utility.generalBorder
import java.util.Date


data class AddExpenseUiState(
    val title: MutableState<String> = mutableStateOf(""),
    val expenseItemListAmountTextWidth: MutableState<Dp> = mutableStateOf(0.dp),
    val totalExpenseAmount: MutableState<Double> = mutableDoubleStateOf(0.0),
    val loadingState : MutableState<LoadingState> = mutableStateOf(LoadingState.IDLE),
    val snackbarText : MutableState<String> = mutableStateOf(""),
    val snackbarState : MutableState<Boolean> = mutableStateOf(false),
)

@Composable
fun AddExpense(
    appState: AppState,
    viewModel: AddExpenseViewModel
) {
    MainUi(viewModel, appState)
}

@Composable
private fun MainUi(
    viewModel: AddExpenseViewModel,
    appState: AppState
) {
    val uiState = viewModel.addExpenseUiState
    val localDensity = LocalDensity.current
    val allExpenseItem = viewModel.allExpenseItem.collectAsState(initial = arrayListOf())
    val screenWidth = LocalConfiguration.current.screenWidthDp

    LaunchedEffect(key1 = allExpenseItem.value.size) {
        if (allExpenseItem.value.isNotEmpty()) {
            allExpenseItem.value.forEach { uiState.totalExpenseAmount.value += it.itemAmount ?: 0.0 }
        } else {
            uiState.totalExpenseAmount.value = 0.0
        }
    }

    LoadingDialog(
        loadingState = uiState.loadingState,
        onSuccess = {
            viewModel.deleteExpenseItemsFromLocalDatabase()
            uiState.title.value = ""
        }
    )

    // This for loop is just fro to calculate the maximum width of the text element.
    // Which I can use in the LazyColumn item to set the Rupee Text width.
    allExpenseItem.value.forEach {
        Text(
            text = getRupeeString(it.itemAmount ?: 0.0),
            modifier = Modifier
                .onGloballyPositioned {
                    val tempDp = with(localDensity) {
                        it.size.width.toDp()
                    }
                    if (tempDp > uiState.expenseItemListAmountTextWidth.value) {
                        uiState.expenseItemListAmountTextWidth.value = tempDp
                    }
                }
                .zIndex(-100f) // zIndex is -1 so that it does not show on the screen.
                .alpha(0f)
        )
    }

    // End

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .pointerInput(Unit) {}
    ) {
        if(screenWidth <= 500) {
            TopAppBar(
                appState = appState,
                btnText = "ADD",
                buttonEnableCondition = true,
                onClick = {
                    addExpense(uiState, allExpenseItem, viewModel)
                }
            )
        } else {
            TopAppBar(appState = appState)
        }

        GeneralSnackBar(
            visible = uiState.snackbarState,
            text = uiState.snackbarText.value,
            containerColor = red_color
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topBarHeight)
                .pointerInput(Unit) {}
        ) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .pointerInput(Unit) {}
                    .background(background)
            ) {

                if(screenWidth <= 500) {
                    UpperTitleSection(
                        appState = appState,
                        title = uiState.title
                    )
                } else {
                    AddExpenseItemTitleSection()
                }

                ItemListLayout(
                    appState,
                    uiState.expenseItemListAmountTextWidth.value,
                    allExpenseItem.value
                )
            }

            if(screenWidth > 500) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {

                    UpperTitleSection(
                        appState = appState,
                        title = uiState.title
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(generalPadding)
                                .padding(horizontal = halfGeneralPadding),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total Amount : ",
                                style = Typography.bodyMedium.copy(color = onSurface)
                            )

                            Text(
                                text = getRupeeString(uiState.totalExpenseAmount.value),
                                style = Typography.bodyMedium.copy(color = onSurface)
                            )
                        }
                        GeneralButton(
                            text = "ADD EXPENSE",
                            modifier = Modifier
                                .padding(bottom = halfGeneralPadding, end = generalPadding)
                                .align(Alignment.BottomEnd)
                        ) {
                            addExpense(uiState, allExpenseItem, viewModel)
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun ItemListLayout(
    appState: AppState,
    rupeeTextWidth: Dp,
    allExpenseItems: List<ExpenseItem>
) {

    Box {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(background)
                .clip(RoundedCornerShape(generalPadding))
                .padding(halfGeneralPadding)
                .generalBorder(color = border_color, width = 0.2.dp)
        ) {
            if (allExpenseItems.isEmpty()) {
                val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_list))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    LottieAnimation(
                        composition = lottieComposition,
                        modifier = Modifier
                            .size(350.dp, 350.dp),
                        contentScale = ContentScale.Fit
                    )
                    Text(
                        text = "No items added!",
                        style = Typography.bodyLarge.copy(color = onBackground)
                    )
                }

            } else {
                LazyColumn {
                    itemsIndexed(allExpenseItems) {index, item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = generalPadding,
                                    start = generalPadding,
                                    end = generalPadding
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item.itemTitle,
                                style = Typography.bodyMedium.copy(color = text_color),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = generalPadding)
                            )

                            Text(
                                text = getRupeeString(item.itemAmount!!),
                                style = Typography.bodyMedium.copy(color = text_color),
                                modifier = Modifier
                                    .width(rupeeTextWidth)
                            )
                        }

                        if(index != allExpenseItems.size - 1) {
                            HorizontalLineOnBackground(
                                modifier = Modifier
                                    .padding(horizontal = generalPadding)
                                    .padding(top = generalPadding)
                            )
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { appState.navController.navigate(Screen.AddExpenseItemScreen.route) },
            shape = RoundedCornerShape(50),
            containerColor = enabled_color,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = generalPadding, end = generalPadding)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = "Add expense item",
                tint = Color.White
            )
        }
    }

}

fun addExpense(
    uiState: AddExpenseUiState,
    allExpenseItem : State<ArrayList<ExpenseItem>>,
    viewModel: AddExpenseViewModel
) {

    if(allExpenseItem.value.isEmpty()) {
        uiState.snackbarText.value = "Please! add expense item."
        Utility.showSnackBar(uiState.snackbarState)
    } else if(uiState.title.value.isEmpty()) {
        uiState.snackbarText.value = "Please! add title."
        Utility.showSnackBar(uiState.snackbarState)
    } else {
        val tempExpense = Expense(
            id = System.currentTimeMillis(),
            title = uiState.title.value,
            items = allExpenseItem.value,
            totalAmount = uiState.totalExpenseAmount.value,
            date = Date(System.currentTimeMillis())
        )
        viewModel.addExpenseIntoLocalDatabase(tempExpense)
        viewModel.deleteExpenseItemsFromLocalDatabase()
    }

}

