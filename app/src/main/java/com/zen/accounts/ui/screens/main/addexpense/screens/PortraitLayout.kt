package com.zen.accounts.ui.screens.main.addexpense.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
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
import com.zen.accounts.ui.screens.common.TopAppBar
import com.zen.accounts.ui.screens.common.getRupeeString
import com.zen.accounts.ui.viewmodels.AddExpenseViewModel
import com.zen.accounts.ui.screens.main.addexpense.HorizontalLineOnBackground
import com.zen.accounts.ui.screens.main.addexpense.UpperTitleSection
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.background
import com.zen.accounts.ui.theme.border_color
import com.zen.accounts.ui.theme.enabled_color
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.halfGeneralPadding
import com.zen.accounts.ui.theme.onBackground
import com.zen.accounts.ui.theme.text_color
import java.util.Date

@Composable
fun PortraitLayout(
    appState: AppState,
    viewModel: AddExpenseViewModel
) {

    val uiState = viewModel.addExpenseUiState
    val localDensity = LocalDensity.current
    val allExpenseItem = viewModel.allExpenseItem.collectAsState(initial = listOf())
    val coroutineScope = rememberCoroutineScope()

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
                .zIndex(-1f) // zIndex is -1 so that it does not show on the screen.
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        TopAppBar(
            appState = appState,
            buttonEnableCondition = allExpenseItem.value.isNotEmpty()
        ) {
            val expense = Expense(
                id = System.currentTimeMillis(),
                title = uiState.title.value,
                items = allExpenseItem.value,
                totalAmount = allExpenseItem.value.reduce { acc, expenseItem ->
                    acc.copy(
                        itemAmount = acc.itemAmount!! + expenseItem.itemAmount!!
                    )
                }.itemAmount!!,
                date = Date(System.currentTimeMillis())
            )
            viewModel.addExpenseIntoLocalDatabase(expense)
        }

        UpperTitleSection(appState = appState, title = uiState.title)

        Box {
            ItemListLayout(
                uiState.expenseItemListAmountTextWidth.value,
                allExpenseItem.value
            )

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

}

@Composable
fun ItemListLayout(
    rupeeTextWidth: Dp,
    allExpenseItems: List<ExpenseItem>
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .clip(RoundedCornerShape(generalPadding))
            .padding(halfGeneralPadding)
            .border(
                width = 0.2.dp,
                color = border_color,
                shape = RoundedCornerShape(generalPadding)
            )
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
}