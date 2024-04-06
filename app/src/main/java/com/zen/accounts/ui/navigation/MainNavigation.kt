package com.zen.accounts.ui.navigation

import android.util.Log
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.DrawerDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zen.accounts.R
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.screens.addexpense.AddExpense
import com.zen.accounts.ui.screens.addexpense.AddExpenseViewModel
import com.zen.accounts.ui.screens.addexpense.addexpenseitem.AddExpenseItem
import com.zen.accounts.ui.screens.myexpense.MyExpense
import com.zen.accounts.ui.screens.myexpense.MyExpenseViewModel
import com.zen.accounts.ui.screens.setting.Setting
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.add_expense_item_screen_route
import com.zen.accounts.ui.theme.background
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.halfGeneralPadding
import com.zen.accounts.ui.theme.main_route
import com.zen.accounts.ui.theme.onBackground
import com.zen.accounts.ui.theme.shadowColor
import com.zen.accounts.ui.theme.surface
import com.zen.accounts.utility.customShadow
import com.zen.accounts.utility.customShadowTwo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainNavigation(appState: AppState, padding: PaddingValues) {
    appState.mainNavController = rememberNavController()
    NavHost(
        navController = appState.mainNavController,
        startDestination = BottomScreen.AddExpenseScreen.route,
        route = main_route
    ) {
        // Add Expense Screen route to navigate
        composable(route = BottomScreen.AddExpenseScreen.route) {
            val viewModel: AddExpenseViewModel = hiltViewModel()
            label = BottomScreen.AddExpenseScreen.title
            AddExpense(appState = appState, viewModel = viewModel, paddingValues = padding)
        }

        composable(route = Screen.AddExpenseItemScreen.route) {
            val viewModel: AddExpenseViewModel = hiltViewModel()
            AddExpenseItem(appState = appState, viewModel = viewModel, paddingValues = padding)
        }

        // My Expense Screen route to navigate
        composable(route = BottomScreen.MyExpenseScreen.route) {
            label = BottomScreen.AddExpenseScreen.title
            val viewModel: MyExpenseViewModel = hiltViewModel()
            MyExpense(appState = appState, viewModel, padding)
        }

        // Setting Screen route to navigate
        composable(route = BottomScreen.SettingScreen.route) {
            Setting(appState = appState)
        }


    }
}

@Composable
fun BottomNavigation(appState: AppState) {
    val items = listOf(
        BottomScreen.MyExpenseScreen,
        BottomScreen.AddExpenseScreen,
        BottomScreen.SettingScreen
    )

    val localConf = LocalConfiguration.current
    val screenWidth = localConf.screenWidthDp
    val topAppBarTitle = remember { mutableStateOf("") }
    val navBackStackEntry by appState.mainNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val coroutineScope = rememberCoroutineScope()

    // Setting Top App Bar title
    LaunchedEffect(key1 = currentDestination?.route) {
        for (item in getAllScreenRouteWithTitle()) {
            if (currentDestination?.route == item.first) {
                topAppBarTitle.value = item.second
            }
        }
    }

    if (screenWidth <= 500) {
        appState.drawerState = null
        Scaffold(
            topBar = if (items.any { it.route == currentDestination?.route }) {
                {}
            } else {
                {
                    TopAppBar(
                        modifier = Modifier
                            .customShadowTwo(),
                        backgroundColor = background
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(generalPadding)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    coroutineScope.launch {
                                        appState.mainNavController.popBackStack()
                                    }
                                },
                            tint = onBackground
                        )

                        Text(text = topAppBarTitle.value)
                    }
                }
            },
            bottomBar = if (items.any { it.route == currentDestination?.route }) {
                {
                    BottomNavigation(
                        backgroundColor = background,
                        modifier = Modifier
                            .customShadowTwo()
                    ) {
                        items.forEach { screen ->
                            BottomNavigationItem(
                                icon = {
                                    Icon(
                                        painter = painterResource(id = screen.icon),
                                        contentDescription = null
                                    )
                                },
                                label = {
                                    Text(
                                        screen.title,
                                        style = Typography.bodySmall
                                    )
                                },
                                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                onClick = {
                                    appState.mainNavController.navigate(screen.route) {
                                        popUpTo(appState.mainNavController.graph.findStartDestination().id) {
                                            saveState = false
                                        }
                                        launchSingleTop = true
                                        restoreState = false
                                    }
                                }
                            )
                        }
                    }
                }
            } else {
                {}
            }
        ) {
            MainNavigation(appState = appState, it)
        }

    } else {
        appState.drawerState = remember { mutableStateOf(false) }
        val transformer = updateTransition(targetState = appState.drawerState, "drawer width")
        val drawerWidth = transformer.animateDp(label = "drawer width") {
            if (it!!.value) {
                400.dp
            } else {
                0.dp
            }
        }

        Box {
            Scaffold(
                topBar = {
                    TopAppBar {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_menu),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(generalPadding)
                                .clickable {
                                    coroutineScope.launch {
                                        appState.drawerState?.value = true
                                    }
                                },
                            tint = onBackground
                        )

                        Text(text = topAppBarTitle.value)
                    }
                }
            ) {
                MainNavigation(appState = appState, it)
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(drawerWidth.value)
                    .background(background),
                horizontalAlignment = Alignment.End
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu_opened),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(generalPadding)
                        .clickable {
                            coroutineScope.launch {
                                appState.drawerState?.value = false
                            }
                        },
                    tint = onBackground
                )
                items.forEach { screen ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                coroutineScope.launch {
                                    delay(200L)
                                    appState.drawerState?.value = false
                                }
                                appState.mainNavController.navigate(screen.route) {
                                    popUpTo(appState.mainNavController.graph.findStartDestination().id) {
                                        saveState = false
                                    }
                                    launchSingleTop = true
                                    restoreState = false
                                }
                            }
                            .background(if (currentDestination?.hierarchy?.any { it.route == screen.route } == true) surface else background)
                            .padding(generalPadding)
                    ) {
                        Text(
                            text = screen.title,
                            style = Typography.titleMedium.copy(color = onBackground)
                        )
                    }
                }
            }
        }
    }
}