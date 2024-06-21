package com.zen.accounts.ui.screens.main.setting


import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zen.accounts.R
import com.zen.accounts.db.model.User
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.screens.common.BackupPlan
import com.zen.accounts.ui.screens.common.GeneralButton
import com.zen.accounts.ui.screens.common.GeneralDialog
import com.zen.accounts.ui.screens.common.GeneralDropDown
import com.zen.accounts.ui.screens.common.GeneralSnackBar
import com.zen.accounts.ui.screens.common.LoadingDialog
import com.zen.accounts.ui.screens.common.LoadingState
import com.zen.accounts.ui.screens.common.TopAppBar
import com.zen.accounts.ui.screens.common.auth_route
import com.zen.accounts.ui.screens.common.login_button_label
import com.zen.accounts.ui.screens.common.login_screen_label
import com.zen.accounts.ui.screens.common.logout_button_label
import com.zen.accounts.ui.screens.common.small_logout_button_label
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.background
import com.zen.accounts.ui.theme.buttonDescriptionTextStyle
import com.zen.accounts.ui.theme.disabled_color
import com.zen.accounts.ui.theme.enabled_color
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.halfGeneralPadding
import com.zen.accounts.ui.theme.light_enabled_color
import com.zen.accounts.ui.theme.onBackground
import com.zen.accounts.ui.theme.onSurface
import com.zen.accounts.ui.theme.red_color
import com.zen.accounts.ui.theme.roundedCornerShape
import com.zen.accounts.ui.theme.surface
import com.zen.accounts.ui.theme.tweenAnimDuration
import com.zen.accounts.ui.viewmodels.SettingViewModel
import com.zen.accounts.utility.LoadingScreen
import com.zen.accounts.utility.Utility
import com.zen.accounts.utility.generalBorder
import com.zen.accounts.utility.generalCircleBorder
import com.zen.accounts.utility.io
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


data class SettingUiState(
    val showBackupDropDown: MutableState<Boolean> = mutableStateOf(false),
    val backupDropDownText: MutableState<BackupPlan> = mutableStateOf(BackupPlan.Off),
    val showSnackBar: MutableState<Boolean> = mutableStateOf(false),
    val showSnackBarJob: MutableState<Job?> = mutableStateOf(null),
    val showSnackBarText: MutableState<String> = mutableStateOf(""),
    val showSnackBarColor: MutableState<Color> = mutableStateOf(red_color),
    val loadingState: MutableState<LoadingState> = mutableStateOf(LoadingState.IDLE),
    val backupLoadingState: MutableState<LoadingState> = mutableStateOf(LoadingState.IDLE),
    val showLogoutPopUp: MutableState<Boolean> = mutableStateOf(false),
    val showConfirmationPopUp: MutableState<Boolean> = mutableStateOf(false),
    val showImagePickerOption: MutableState<Boolean> = mutableStateOf(false),
    val profilePic: MutableState<Bitmap?> = mutableStateOf(null)
)

@Composable
fun Setting(
    appState: AppState,
    settingViewModel: SettingViewModel
) {
    val uiState = settingViewModel.settingUIState

    LaunchedEffect(key1 = Unit) {
        settingViewModel.getBackupPlan()
    }

    LoadingDialog(loadingState = uiState.loadingState)

    MainUI(
        appState = appState,
        settingViewModel = settingViewModel
    )
}

@Composable
private fun MainUI(
    appState: AppState,
    settingViewModel: SettingViewModel
) {
    val uiState = settingViewModel.settingUIState
    val coroutineScope = rememberCoroutineScope()
    val darkSwitchOn = appState.darkMode.value ?: isSystemInDarkTheme()
    val screenWidth = LocalConfiguration.current.screenWidthDp
    BackHandler(uiState.showImagePickerOption.value) {
        uiState.showImagePickerOption.value = false
    }

    var rotation by remember { mutableFloatStateOf(0f) }
    val infiniteTransition = rememberInfiniteTransition(label = "backup indicator label")

    if(uiState.backupLoadingState.value == LoadingState.LOADING){
        rotation = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                tween(1000),
                repeatMode = RepeatMode.Restart
            ),
            label = "rotation label"
        ).value
    }

    ScreenDialogs(
        settingViewModel
    )

    Box(
        modifier = Modifier.fillMaxSize()
            .pointerInput(Unit) {}
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(background)
        ) {
            TopAppBar(
                appState = appState
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val user = settingViewModel.user.collectAsState(initial = null)
                Crossfade(
                    targetState = user.value,
                    animationSpec = tween(500),
                    label = "main screen"
                ) {
                    if(it == null) {
                        LoadingScreen()
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(background),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if(screenWidth <= 500) {
                                ProfileSection(
                                    user,
                                    uiState.showImagePickerOption,
                                    uiState.profilePic
                                )
                            }

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
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Backup",
                                    style = Typography.bodyMedium.copy(color = onBackground)
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                AnimatedVisibility(
                                    visible = uiState.backupLoadingState.value == LoadingState.LOADING,
                                    modifier = Modifier,
                                    enter = fadeIn(),
                                    exit = fadeOut()
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_sync),
                                        "backup indicator",
                                        modifier = Modifier
                                            .size(20.dp)
                                            .graphicsLayer {
                                                rotationZ = rotation
                                            }
                                    )
                                }

                                GeneralDropDown(
                                    modifier = Modifier.width(160.dp),
                                    value = uiState.backupDropDownText,
                                    showDropDown = uiState.showBackupDropDown,
                                    valueList = BackupPlan.getAllBackupPlan(),
                                    enable = user.value!!.isAuthenticated,
                                    onClick = {
                                        if (user.value!!.uid.isEmpty() && !user.value!!.isAuthenticated) {
                                            uiState.showSnackBarText.value =
                                                "Please login to use backup"
                                            coroutineScope.launch {
                                                if (uiState.showSnackBarJob.value == null || (uiState.showSnackBarJob.value != null && !uiState.showSnackBarJob.value!!.isActive)) {
                                                    uiState.showSnackBarJob.value =
                                                        coroutineScope.launch(Dispatchers.IO) {
                                                            Utility.showSnackBar(uiState.showSnackBar)
                                                        }
                                                }
                                            }
                                        }
                                    },
                                    onItemClick = { backupPlan ->
                                        uiState.backupDropDownText.value = backupPlan
                                        coroutineScope.launch(Dispatchers.IO) {
                                            when (backupPlan) {
                                                is BackupPlan.Off -> {
                                                    settingViewModel.cancelAllWork()
                                                }

                                                is BackupPlan.Now -> {
                                                    settingViewModel.startSingleUploadRequest()
                                                    settingViewModel.updateBackupPlan()
                                                }

                                                is BackupPlan.Daily -> {
                                                    settingViewModel.startDailyUploadRequest()
                                                    settingViewModel.updateBackupPlan()
                                                }

                                                is BackupPlan.Weekly -> {
                                                    settingViewModel.startWeeklyUploadRequest()
                                                    settingViewModel.updateBackupPlan()
                                                }

                                                is BackupPlan.Monthly -> {
                                                    settingViewModel.startMonthlyUploadRequest()
                                                    settingViewModel.updateBackupPlan()
                                                }
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
                                    text = if (user.value!!.isAuthenticated) small_logout_button_label else login_screen_label,
                                    style = Typography.bodyMedium.copy(color = onBackground)
                                )

                                GeneralButton(
                                    text = if (user.value!!.isAuthenticated) logout_button_label else login_button_label,
                                    modifier = Modifier
                                        .width(160.dp)
                                        .padding(
                                            horizontal = generalPadding,
                                            vertical = halfGeneralPadding
                                        )
                                ) {
                                    coroutineScope.launch {
                                        val currentUser = appState.dataStore.getUser()
                                        if (currentUser != null && currentUser.isAuthenticated)
                                            uiState.showLogoutPopUp.value = true
                                        else
                                            appState.navController.navigate(auth_route)
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = uiState.showImagePickerOption.value,
            modifier = Modifier
                .align(Alignment.BottomCenter),
            enter = slideInVertically(tween(tweenAnimDuration)) { it },
            exit = slideOutVertically(tween(tweenAnimDuration)) { it }
        ) {
            ImagePickerSection(
                modifier = Modifier
                    .background(surface),
                settingViewModel
            )
        }

        GeneralSnackBar(
            visible = uiState.showSnackBar,
            text = uiState.showSnackBarText.value,
            modifier = Modifier.align(Alignment.TopCenter),
            containerColor = uiState.showSnackBarColor.value
        )
    }
}

@Composable
fun ScreenDialogs(
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
            .alpha(0f)
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

@Composable
fun ProfileSection(
    user: State<User?>,
    showImagePickerOption: MutableState<Boolean>,
    profilePicBitmap: MutableState<Bitmap?>
) {
    var name = "John Doe"
    var mobile = "1234567890"
    var email = "johndoe@example.com"


    if (user.value != null && user.value!!.name.isNotEmpty()) {
        name = user.value!!.name
    }
    if (user.value != null && user.value!!.phone.isNotEmpty()) {
        mobile = user.value!!.phone
    }
    if (user.value != null && user.value!!.email.isNotEmpty()) {
        email = user.value!!.email
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(surface)
            .padding(bottom = generalPadding, top = halfGeneralPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(halfGeneralPadding)
                .clip(shape = CircleShape)
                .clickable {
                    showImagePickerOption.value = true
                }
                .background(Color.LightGray)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(profilePicBitmap.value)
                    .crossfade(true)
                    .build(),
                contentDescription = "profile image",
                placeholder = painterResource(id = R.drawable.ic_person),
                error = painterResource(id = R.drawable.ic_person),
                modifier = Modifier
                    .size(120.dp)
            )

            Text(
                text = "Edit",
                style = Typography.bodySmall.copy(color = background),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(120.dp)
                    .align(Alignment.BottomCenter)
                    .background(Color.LightGray.copy(alpha = 0.5f))
                    .padding(bottom = 5.dp)
            )
        }


        Text(
            text = name,
            textAlign = TextAlign.Center,
            style = Typography.bodyMedium.copy(color = onBackground)
        )
        Text(
            text = "+91 $mobile",
            textAlign = TextAlign.Center,
            style = Typography.bodyMedium.copy(color = onBackground)
        )
        Text(
            text = email,
            textAlign = TextAlign.Center,
            style = Typography.bodyMedium.copy(color = onBackground)
        )
    }
}

@Composable
fun ImagePickerSection(
    modifier: Modifier,
    settingViewModel: SettingViewModel
) {
    val context = LocalContext.current
    val includeGallery = remember { mutableStateOf(false) }
    val launcher = Utility.imagePicker {
        io {
            val bitmap = settingViewModel.saveImageToStorage(it)
            bitmap.await()?.let { it1 -> settingViewModel.uploadUserProfilePicture(it1) }
        }
    }

    val requestLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
            if (it) {
                Utility.imageCropLauncher(launcher, includeGallery.value)
            }
        }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .then(modifier),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .generalCircleBorder(size = 100.dp)
                .clickable {
                    includeGallery.value = false
                    if (context.checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestLauncher.launch(android.Manifest.permission.CAMERA)
                    } else {
                        Utility.imageCropLauncher(launcher)
                    }
                }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_camera),
                contentDescription = null,
                tint = onSurface,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(generalPadding.times(2)))

        Box(
            modifier = Modifier
                .size(100.dp)
                .generalCircleBorder(size = 100.dp)
                .clickable {
                    includeGallery.value = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (context.checkSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                            requestLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                        } else {
                            Utility.imageCropLauncher(launcher, true)
                        }
                    } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                        if (context.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        } else {
                            Utility.imageCropLauncher(launcher, true)
                        }
                    }
                }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_gallery),
                contentDescription = null,
                tint = onSurface,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(24.dp)
            )
        }
    }
}