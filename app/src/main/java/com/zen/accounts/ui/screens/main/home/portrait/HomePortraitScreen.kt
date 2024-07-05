package com.zen.accounts.ui.screens.main.home.portrait

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.zen.accounts.R
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.navigation.Screen
import com.zen.accounts.ui.screens.common.add_expense_screen_label
import com.zen.accounts.ui.screens.common.getRupeeString
import com.zen.accounts.ui.screens.common.home_screen_label
import com.zen.accounts.ui.screens.common.my_expense_screen_label
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.background
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.halfGeneralPadding
import com.zen.accounts.ui.theme.onBackground
import com.zen.accounts.ui.theme.primary_color
import com.zen.accounts.ui.theme.secondary_color
import com.zen.accounts.ui.viewmodels.HomeViewModel
import com.zen.accounts.utility.main
import kotlinx.coroutines.launch

@Composable
fun HomePortraitScreen(
    appState: AppState,
    viewModel: HomeViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val uiState = viewModel.homeUiState
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(generalPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = R.drawable.ic_logo,
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )

            Spacer(modifier = Modifier.width(halfGeneralPadding))

            Text(
                text = home_screen_label,
                style = Typography.headlineSmall.copy(color = onBackground),
                modifier = Modifier
                    .padding(vertical = halfGeneralPadding)
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.ic_setting),
                contentDescription = "setting icon",
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .clickable {
                        coroutineScope.launch {
                            appState.navController.navigate(Screen.SettingScreen.route)
                        }
                    }
                    .background(secondary_color)
                    .padding(halfGeneralPadding)
                ,
                tint = primary_color
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(generalPadding)
                .clickable {
                    main {
                        appState.navigate(Screen.MonthlyExpenseScreen.route)
                    }
                }
                .clip(RoundedCornerShape(generalPadding))
                .background(secondary_color)
                .padding(horizontal = generalPadding, vertical = generalPadding.times(2)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Text(
                text = "Monthly Expense",
                style = Typography.bodyLarge.copy(color = onBackground)
            )

            Text(
                text = getRupeeString(uiState.totalAmount.value, showZero = true),
                style = Typography.bodyLarge.copy(color = onBackground)
            )
        }

        Row(
            modifier = Modifier
                .padding(start = generalPadding)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = generalPadding, end = generalPadding)
                    .clickable {
                        coroutineScope.launch {
                            appState.navigate(Screen.AddExpenseScreen.route)
                        }
                    }
                    .clip(shape = RoundedCornerShape(generalPadding))
                    .background(primary_color)
                    .padding(horizontal = generalPadding, vertical = generalPadding.times(2))
            ) {
                Text(
                    text = add_expense_screen_label,
                    style = Typography.bodySmall.copy(color = Color.White),
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = generalPadding, end = generalPadding)
                    .clickable {
                        main {
                            appState.navigate(Screen.MyExpenseScreen.route)
                        }
                    }
                    .clip(shape = RoundedCornerShape(generalPadding))
                    .background(primary_color)
                    .padding(horizontal = generalPadding, vertical = generalPadding.times(2))
            ) {
                Text(
                    text = my_expense_screen_label,
                    style = Typography.bodySmall.copy(color = Color.White),
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }

        }
    }
}