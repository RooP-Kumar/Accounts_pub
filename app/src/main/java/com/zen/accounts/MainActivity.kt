package com.zen.accounts

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zen.accounts.states.AppState
import com.zen.accounts.states.getAppState
import com.zen.accounts.ui.navigation.NavigationGraph
import com.zen.accounts.ui.navigation.getScreenRouteWithIcon
import com.zen.accounts.ui.screens.common.auth_route
import com.zen.accounts.ui.screens.common.small_logout_button_label
import com.zen.accounts.ui.screens.main.addexpense.HorizontalLineOnBackground
import com.zen.accounts.ui.screens.main.setting.ProfileSection
import com.zen.accounts.ui.screens.main.setting.ScreenDialogs
import com.zen.accounts.ui.theme.AccountsThemes
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.background
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.onBackground
import com.zen.accounts.ui.theme.onSurface
import com.zen.accounts.ui.theme.surface
import com.zen.accounts.ui.viewmodels.SettingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val settingViewModel: SettingViewModel by viewModels<SettingViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val context = LocalContext.current
            val appState = getAppState(context)
            appState.darkMode = appState.dataStore.isInDarkMode.collectAsState(initial = null)
            appState.navController = navController
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            appState.drawerState.value = drawerState

            MainUI(
                appState,
                drawerState,
                settingViewModel
            )
        }
    }
}

@Composable
private fun MainUI(
    appState: AppState,
    drawerState: DrawerState,
    settingViewModel: SettingViewModel
) {
    AccountsThemes {
        val screenWidth = LocalConfiguration.current.screenWidthDp.dp
        val coroutineScope = rememberCoroutineScope()

        if (screenWidth <= 500.dp) {
            NavigationGraph(appState)
        } else {
            val user = appState.dataStore.getUser.collectAsState(initial = null)
            val screens = getScreenRouteWithIcon(user.value?.isAuthenticated ?: false)
            val backStackEntry = appState.navController.currentBackStackEntryAsState()
            val currentDestination = backStackEntry.value?.destination?.route
            val showImagePickerOption = remember { mutableStateOf(false) }
            val profilePicBitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }

            val profilePic = appState.dataStore.getProfilePic.collectAsState(initial = null)
            LaunchedEffect(key1 = profilePic.value) {
                withContext(Dispatchers.IO) {
                    if (profilePic.value != null) {
                        profilePicBitmap.value = BitmapFactory.decodeByteArray(
                            profilePic.value,
                            0,
                            profilePic.value!!.size
                        )
                    }
                }
            }

            val scrollState = rememberScrollState()
            val uiState = settingViewModel.settingUIState

            Box(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    gesturesEnabled = false,
                    drawerContent = {
                        ModalDrawerSheet(
                            drawerContainerColor = background,
                            drawerContentColor = onBackground
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    "Profile",
                                    style = Typography.bodyMedium.copy(color = onBackground),
                                    modifier = Modifier.padding(generalPadding)
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                IconButton(onClick = {
                                    coroutineScope.launch {
                                        appState.drawerState.value?.close()
                                    }
                                }) {
                                    Icon(
                                        painterResource(id = R.drawable.ic_menu_opened),
                                        contentDescription = "menu close button"
                                    )
                                }
                            }

                            HorizontalLineOnBackground()

                            Column(
                                modifier = Modifier
                                    .verticalScroll(
                                        state = scrollState
                                    )
                            ) {
                                ProfileSection(
                                    user = user,
                                    showImagePickerOption = showImagePickerOption,
                                    profilePicBitmap = profilePicBitmap
                                )
                                screens.forEach {
                                    NavigationDrawerItem(
                                        modifier = Modifier
                                            .padding(horizontal = generalPadding)
                                            .padding(top = generalPadding),
                                        colors = NavigationDrawerItemDefaults.colors(
                                            selectedContainerColor = surface,
                                            selectedIconColor = onBackground,
                                            unselectedContainerColor = background,
                                            unselectedIconColor = onBackground
                                        ),
                                        icon = {
                                            Icon(
                                                painterResource(id = it.second),
                                                contentDescription = "setting button",
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = it.first.title,
                                                style = Typography.bodyMedium.copy(color = onSurface)
                                            )
                                        },
                                        selected = currentDestination == it.first.route,
                                        onClick = {
                                            coroutineScope.launch {
                                                if (it.first.route != small_logout_button_label) appState.drawerNavigate(
                                                    it.first.route
                                                )
                                                else {
                                                    coroutineScope.launch {
                                                        val currentUser =
                                                            appState.dataStore.getUser()
                                                        if (currentUser != null && currentUser.isAuthenticated)
                                                            uiState.showLogoutPopUp.value = true
                                                        else
                                                            appState.navController.navigate(
                                                                auth_route
                                                            )
                                                    }
                                                }
                                            }
                                        }
                                    )
                                }
                                Spacer(modifier = Modifier.height(generalPadding))
                            }

                        }
                    }
                ) {
                    NavigationGraph(appState)
                }

                ScreenDialogs(settingViewModel = settingViewModel)
            }
        }
    }
}
