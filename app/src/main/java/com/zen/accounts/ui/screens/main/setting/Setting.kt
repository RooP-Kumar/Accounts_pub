package com.zen.accounts.ui.screens.main.setting


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.work.WorkInfo
import com.zen.accounts.db.model.User
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.screens.common.*
import com.zen.accounts.ui.viewmodels.AddExpenseViewModel
import com.zen.accounts.ui.theme.*
import com.zen.accounts.ui.viewmodels.SettingViewModel
import com.zen.accounts.utility.generalBorder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


data class SettingUiState(
    val showBackupDropDown: MutableState<Boolean> = mutableStateOf(false),
    val backupDropDownText: MutableState<BackupPlan> = mutableStateOf(BackupPlan.Off),
    val showSnackBar: MutableState<Boolean> = mutableStateOf(false),
    val showSnackBarJob: MutableState<Job?> = mutableStateOf(null),
    val showSnackBarText: MutableState<String> = mutableStateOf(""),
    val loadingState: MutableState<LoadingState> = mutableStateOf(LoadingState.IDLE),
    val showLogoutPopUp: MutableState<Boolean> = mutableStateOf(false),
    val showConfirmationPopUp: MutableState<Boolean> = mutableStateOf(false)
)

@Composable
fun Setting(
    appState: AppState,
    addExpenseViewModel: AddExpenseViewModel,
    settingViewModel: SettingViewModel
) {
    val context = LocalContext.current
    val uiState = settingViewModel.settingUIState
    val workState = addExpenseViewModel.uploadExpenseWorkerInfo.collectAsState(initial = listOf())
    val user = appState.dataStore.getUser.collectAsState(initial = null)
    LaunchedEffect(key1 = Unit) {
        settingViewModel.getBackupPlan()
    }

    DisposableEffect(key1 = workState.value) {
        if (workState.value!!.isNotEmpty()) {
            val outputData = workState.value!!.last().outputData.getString(work_manager_output_data)
            when (workState.value!!.last().state) {
                WorkInfo.State.RUNNING -> {
                    uiState.loadingState.value = LoadingState.LOADING
                }

                WorkInfo.State.ENQUEUED -> {
                    uiState.loadingState.value = LoadingState.IDLE
                }

                WorkInfo.State.SUCCEEDED -> {
                    uiState.loadingState.value = LoadingState.SUCCESS
                    uiState.showSnackBarText.value = "Backup Successfully."
                    settingViewModel.showSnackBar()
                    uiState.backupDropDownText.value = BackupPlan.Off
                }

                WorkInfo.State.FAILED -> {
                    uiState.loadingState.value = LoadingState.FAILURE
                    uiState.showSnackBarText.value = outputData.toString()
                    settingViewModel.showSnackBar()
                    uiState.backupDropDownText.value = BackupPlan.Off
                }

                else -> {}
            }

        }
        onDispose {
            uiState.showSnackBar.value = false
        }
    }

    LoadingDialog(loadingState = uiState.loadingState)

    MainUI(
        appState = appState,
        addExpenseViewModel = addExpenseViewModel,
        settingViewModel = settingViewModel,
        user = user
    )
}

@Composable
private fun MainUI(
    appState: AppState,
    addExpenseViewModel: AddExpenseViewModel,
    settingViewModel: SettingViewModel,
    user: State<User?>
) {
    val uiState = settingViewModel.settingUIState
    val coroutineScope = rememberCoroutineScope()
    val darkSwitchOn = appState.darkMode.value ?: isSystemInDarkTheme()

    ScreenDialogs(
        settingViewModel
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(appState = appState)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(generalPadding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Dark Mode",
                    style = Typography.bodyMedium.copy(color = onBackground)
                )

                Switch(
                    checked = darkSwitchOn,
                    onCheckedChange = {
                        coroutineScope.launch {
                            appState.dataStore.saveIsDarkMode(!darkSwitchOn)
                        }
                    },
                    colors = SwitchDefaults.colors().copy(
                        checkedBorderColor = onBackground,
                        checkedThumbColor = onBackground,
                        checkedTrackColor = enabled_color,
                        uncheckedBorderColor = enabled_color,
                        uncheckedThumbColor = enabled_color,
                        uncheckedTrackColor = disabled_color
                    )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = generalPadding, bottom = generalPadding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Backup",
                    style = Typography.bodyMedium.copy(color = onBackground)
                )

                GeneralDropDown(
                    modifier = Modifier.width(160.dp),
                    value = uiState.backupDropDownText,
                    showDropDown = uiState.showBackupDropDown,
                    valueList = BackupPlan.getAllBackupPlan(),
                    enable = user.value != null && user.value!!.isAuthenticated,
                    onClick = {
                        if (user.value == null || (user.value != null && !user.value!!.isAuthenticated)) {
                            uiState.showSnackBarText.value = "Please login to use backup"
                            coroutineScope.launch {
                                if (uiState.showSnackBarJob.value == null || (uiState.showSnackBarJob.value != null && !uiState.showSnackBarJob.value!!.isActive)) {
                                    uiState.showSnackBarJob.value =
                                        coroutineScope.launch(Dispatchers.IO) {
                                            settingViewModel.showSnackBar()
                                        }
                                }
                            }
                        }
                    },
                    onItemClick = {
                        uiState.backupDropDownText.value = it
                        coroutineScope.launch(Dispatchers.IO) {
                            when (it) {
                                is BackupPlan.Now -> {
                                    addExpenseViewModel.startSingleUploadRequest()
                                    settingViewModel.updateBackupPlan()
                                }

                                is BackupPlan.Daily -> {
                                    addExpenseViewModel.startDailyUploadRequest()
                                    settingViewModel.updateBackupPlan()
                                }

                                is BackupPlan.Weekly -> {
                                    addExpenseViewModel.startWeeklyUploadRequest()
                                    settingViewModel.updateBackupPlan()
                                }

                                is BackupPlan.Monthly -> {
                                    addExpenseViewModel.startMonthlyUploadRequest()
                                    settingViewModel.updateBackupPlan()
                                }

                                else -> {}
                            }
                        }
                    }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = generalPadding, bottom = generalPadding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (user.value != null && user.value!!.isAuthenticated) small_logout_button_label else login_screen_label,
                    style = Typography.bodyMedium.copy(color = onBackground)
                )

                GeneralButton(
                    text = if (user.value != null && user.value!!.isAuthenticated) logout_button_label else login_button_label,
                    modifier = Modifier
                        .width(160.dp)
                        .padding(horizontal = generalPadding, vertical = halfGeneralPadding)
                ) {
                    coroutineScope.launch {
                        val currentUser = settingViewModel.dataStore.getUser()
                        currentUser?.let {
                            if (it.isAuthenticated)
                                uiState.showLogoutPopUp.value = true
                            else
                                appState.navController.navigate(auth_route)
                        }
                    }

                }
            }
        }

        GeneralSnackBar(
            visible = uiState.showSnackBar,
            text = uiState.showSnackBarText.value,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
private fun ScreenDialogs(
    settingViewModel: SettingViewModel
) {
    val uiState = settingViewModel.settingUIState
    val localDensity = LocalDensity.current
    val confirmationPopupButtonsHeight = remember { mutableStateOf(0.dp) }


    // Only for getting equal height of the button
    // Start --->
    Button(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = generalPadding)
            .onGloballyPositioned {
                confirmationPopupButtonsHeight.value = with(localDensity) {
                    it.size.height.toDp()
                }
            }
            .zIndex(-100f) // because should not be visible on the screen
    ) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = logout_button_label,
                style = Typography.bodyMedium.copy(color = onBackground),
                modifier = Modifier
                    .padding(top = generalPadding)
            )

            Text(
                text = "Not backed up expenses will be removed permanently.",
                textAlign = TextAlign.Center,
                style = buttonDescriptionTextStyle.copy(color = onBackground),
                modifier = Modifier
                    .padding(bottom = generalPadding, top = halfGeneralPadding)
            )
        }
    }

    // <--- End

    GeneralDialog(
        showDialog = uiState.showLogoutPopUp,
        dialogProperties = DialogProperties(dismissOnClickOutside = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .generalBorder()
                .background(background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "You want to Logout\nAre you sure?",
                textAlign = TextAlign.Center,
                maxLines = 2,
                style = Typography.bodyMedium.copy(color = onBackground),
                modifier = Modifier
                    .padding(generalPadding)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = generalPadding)
                    .padding(bottom = generalPadding)
            ) {
                GeneralButton(text = "CANCEL", modifier = Modifier.weight(1f)) {
                    uiState.showLogoutPopUp.value = false
                }

                Spacer(modifier = Modifier.width(generalPadding))

                GeneralButton(text = "YES", modifier = Modifier.weight(1f)) {
                    uiState.showLogoutPopUp.value = false
                    settingViewModel.logout()
                }
            }
        }
    }

    GeneralDialog(
        showDialog = uiState.showConfirmationPopUp,
        dialogProperties = DialogProperties(dismissOnClickOutside = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .generalBorder()
                .background(background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Warning!\nYou have some expense which are not backed up yet.",
                textAlign = TextAlign.Center,
                style = Typography.bodyMedium.copy(color = onBackground),
                modifier = Modifier
                    .padding(generalPadding)
            )

            Button(
                onClick = {
                    uiState.showConfirmationPopUp.value = false
                    settingViewModel.logoutConfirmation(true)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = generalPadding),
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = Color(0xFFFF204E)
                ),
                shape = roundedCornerShape
            ) {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = logout_button_label,
                        style = Typography.bodyMedium.copy(color = onBackground),
                        modifier = Modifier
                            .padding(top = generalPadding)
                    )

                    Text(
                        text = "Not backed up expenses will be removed permanently.",
                        textAlign = TextAlign.Center,
                        style = buttonDescriptionTextStyle.copy(color = onBackground),
                        modifier = Modifier
                            .padding(bottom = generalPadding, top = halfGeneralPadding)
                    )
                }
            }

            Spacer(modifier = Modifier.height(generalPadding))

            if (confirmationPopupButtonsHeight.value != 0.dp) {
                Button(
                    onClick = {
                        uiState.showConfirmationPopUp.value = false
                        settingViewModel.logoutConfirmation(false)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(confirmationPopupButtonsHeight.value)
                        .padding(horizontal = generalPadding),
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = Color(0xFFA2FF86).copy(alpha = 0.75f)
                    ),
                    shape = roundedCornerShape
                ) {
                    Column(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "BACKUP",
                            style = Typography.bodyMedium.copy(color = onBackground),
                            modifier = Modifier
                                .padding(top = generalPadding)
                        )

                        Text(
                            text = "Backup before logout.",
                            style = buttonDescriptionTextStyle.copy(color = onBackground),
                            modifier = Modifier
                                .padding(bottom = generalPadding, top = halfGeneralPadding)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(generalPadding))

        }
    }
}