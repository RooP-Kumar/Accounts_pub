package com.zen.accounts.ui.screens.main.home.landscape

import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import com.zen.accounts.R
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.navigation.Screen
import com.zen.accounts.ui.screens.common.add_expense_screen_label
import com.zen.accounts.ui.screens.common.getRupeeString
import com.zen.accounts.ui.screens.common.home_screen_label
import com.zen.accounts.ui.screens.common.my_expense_screen_label
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.disabled_color
import com.zen.accounts.ui.theme.enabled_color
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.halfGeneralPadding
import com.zen.accounts.ui.theme.roundedCornerShape
import com.zen.accounts.ui.viewmodels.HomeViewModel
import com.zen.accounts.ui.viewmodels.SettingViewModel
import com.zen.accounts.utility.main
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun HomeLandscapeScreen(
    appState: AppState,
    viewModel: HomeViewModel,
    settingViewModel: SettingViewModel
) {

    val uiState = viewModel.homeUiState

    uiState.user.value = appState.dataStore.getUser.collectAsState(initial = null).value
    LaunchedEffect(key1 = Unit) {
        settingViewModel.getBackupPlan()
    }
    val profilePic = appState.dataStore.getProfilePic.collectAsState(initial = null)
    LaunchedEffect(key1 = profilePic.value) {
        withContext(Dispatchers.IO) {
            if(profilePic.value != null) {
                uiState.profilePic.value = BitmapFactory.decodeByteArray(profilePic.value, 0, profilePic.value!!.size)
            }
        }
    }

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .pointerInput(Unit) {}
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = generalPadding)
            ) {
                IconButton(onClick = {
                    if(appState.drawerState.value != null) {
                        if(appState.drawerState.value!!.isClosed)
                            coroutineScope.launch {
                                appState.drawerState.value!!.open()
                            }
                        else
                            coroutineScope.launch {
                                appState.drawerState.value!!.open()
                            }
                    }
                }) {
                    Icon(
                        painterResource(id = R.drawable.ic_menu),
                        contentDescription = "menu button"
                    )
                }

                Spacer(modifier = Modifier.width(generalPadding))

                Text(
                    text = home_screen_label,
                    style = Typography.headlineSmall.copy(color = MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier
                        .padding(halfGeneralPadding)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(generalPadding),

                ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(roundedCornerShape)
                        .background(MaterialTheme.colorScheme.surface)
                ) {
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
                            .background(disabled_color)
                            .padding(
                                horizontal = generalPadding,
                                vertical = generalPadding.times(2)
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Absolute.SpaceBetween
                    ) {
                        Text(
                            text = "Monthly Expense",
                            style = Typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground)
                        )

                        Text(
                            text = getRupeeString(uiState.totalAmount.value, showZero = true),
                            style = Typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(generalPadding))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(roundedCornerShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(generalPadding)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                coroutineScope.launch {
                                    appState.navigate(Screen.AddExpenseScreen.route)
                                }
                            }
                            .clip(shape = RoundedCornerShape(generalPadding))
                            .background(enabled_color)
                            .padding(
                                horizontal = generalPadding,
                                vertical = generalPadding.times(2)
                            )
                    ) {
                        Text(
                            text = add_expense_screen_label,
                            style = Typography.bodySmall.copy(color = Color.White),
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }

                    Spacer(modifier = Modifier.height(generalPadding))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                main {
                                    appState.navigate(Screen.MyExpenseScreen.route)
                                }
                            }
                            .clip(shape = RoundedCornerShape(generalPadding))
                            .background(enabled_color)
                            .padding(
                                horizontal = generalPadding,
                                vertical = generalPadding.times(2)
                            )
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
    }
}