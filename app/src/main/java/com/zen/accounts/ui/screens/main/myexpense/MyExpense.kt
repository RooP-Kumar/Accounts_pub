package com.zen.accounts.ui.screens.main.myexpense

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.zen.accounts.R
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.navigation.Screen
import com.zen.accounts.ui.screens.common.TopAppBar
import com.zen.accounts.ui.screens.common.date_formatter_pattern_with_time
import com.zen.accounts.ui.screens.common.date_formatter_pattern_without_time
import com.zen.accounts.ui.screens.common.getRupeeString
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.background
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.halfGeneralPadding
import com.zen.accounts.ui.theme.onBackground
import com.zen.accounts.ui.theme.onSurface
import com.zen.accounts.ui.theme.surface
import com.zen.accounts.ui.viewmodels.MyExpenseViewModel
import com.zen.accounts.utility.DateStringConverter
import com.zen.accounts.utility.ExpenseItemLayout
import com.zen.accounts.utility.customShadow
import com.zen.accounts.utility.generalBorder
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class MyExpenseUiState(
    val expenseItemListAmountTextWidth : MutableState<Dp> = mutableStateOf(0.dp)
)

@Composable
fun MyExpense(
    appState: AppState,
    viewModel: MyExpenseViewModel
) {
    val localDensity = LocalDensity.current
    val uiState = viewModel.myExpenseUiState
    val allExpense = viewModel.allExpense.collectAsState(initial = listOf())
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        TopAppBar(appState = appState)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(background)
        ) {
            if(allExpense.value.isEmpty()){
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
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = generalPadding)
                        .padding(top = generalPadding)
                ) {

                    items(allExpense.value.size, key = { it.hashCode() }) {

                        Column(

                            modifier = Modifier
                                .padding(vertical = halfGeneralPadding)
                                .generalBorder()
                                .clickable {
                                    coroutineScope.launch {
                                        appState.navController.navigate(Screen.ExpenseDetailScreen.getRoute(allExpense.value[it]))
                                    }
                                }
                                .background(background)
                                .padding(generalPadding)

                        ) {

                            Row(

                                modifier = Modifier
                                    .fillMaxWidth()

                            ) {

                                Text(
                                    text = allExpense.value[it].title,
                                    style = Typography.bodyLarge.copy(color = onBackground),
                                    modifier = Modifier
                                        .weight(1f)
                                )

                                Text(
                                    text = getRupeeString(allExpense.value[it].totalAmount),
                                    style = Typography.bodyLarge.copy(color = onBackground)
                                )

                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(generalPadding)
                                ,
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "items",
                                    style = Typography.bodyMedium.copy(color = onBackground)
                                )

                                Text(
                                    text = allExpense.value[it].items.size.toString(),
                                    style = Typography.bodyMedium.copy(color = onBackground)
                                )
                            }

                            Text(
                                text = dateString(allExpense.value[it].date),
                                style = Typography.bodyMedium.copy(color = onBackground)
                            )

                        }

                    }

                }
            }
        }
    }



}

private fun dateString(date : Date) : String {
    val formatter = SimpleDateFormat(date_formatter_pattern_without_time, java.util.Locale.UK)
    return formatter.format(date)
}