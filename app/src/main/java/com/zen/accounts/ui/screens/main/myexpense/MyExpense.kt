package com.zen.accounts.ui.screens.main.myexpense

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.zen.accounts.R
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.navigation.Screen
import com.zen.accounts.ui.screens.common.LoadingDialog
import com.zen.accounts.ui.screens.common.LoadingState
import com.zen.accounts.ui.screens.common.TopAppBar
import com.zen.accounts.ui.screens.common.date_formatter_pattern_without_time
import com.zen.accounts.ui.screens.common.getRupeeString
import com.zen.accounts.ui.screens.main.addexpense.HorizontalLineOnBackground
import com.zen.accounts.ui.screens.main.expense_detail.ExpenseItemDeleteDialog
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.background
import com.zen.accounts.ui.theme.enabled_color
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.halfGeneralPadding
import com.zen.accounts.ui.theme.light_enabled_color
import com.zen.accounts.ui.theme.onBackground
import com.zen.accounts.ui.theme.onSurface
import com.zen.accounts.ui.theme.surface
import com.zen.accounts.ui.theme.tweenAnimDuration
import com.zen.accounts.ui.viewmodels.MyExpenseViewModel
import com.zen.accounts.utility.generalBorder
import com.zen.accounts.utility.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

data class MyExpenseUiState(
    val expenseItemListAmountTextWidth: MutableState<Dp> = mutableStateOf(0.dp),
    val showSelectCheckbox: MutableState<Boolean> = mutableStateOf(false),
    val checkBoxList: SnapshotStateList<Boolean> = mutableStateListOf(),
    val selectAll: MutableState<Boolean> = mutableStateOf(false),
    val totalSelectedItem: MutableState<Int> = mutableIntStateOf(1),
    val loadingState: MutableState<LoadingState> = mutableStateOf(LoadingState.IDLE),
    val showDeleteDialog: MutableState<Boolean> = mutableStateOf(false),
    val showExpenseList: MutableState<Boolean> = mutableStateOf(false),
)

@Composable
fun MyExpense(
    appState: AppState,
    viewModel: MyExpenseViewModel,
    isMonthlyExpense: Boolean
) {
    val uiState = viewModel.myExpenseUiState
    val allExpense =
        if (!isMonthlyExpense) viewModel.allExpense.collectAsState(initial = listOf())
        else viewModel.monthlyExpense.collectAsState(initial = listOf())

    BackHandler(uiState.showSelectCheckbox.value) {
        uiState.totalSelectedItem.value = 1
        launch {
            for (i in 0..<uiState.checkBoxList.size) {
                uiState.checkBoxList[i] =
                    uiState.selectAll.value
            }
        }
        uiState.showSelectCheckbox.value = false
    }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = allExpense.value) {
        if (allExpense.value.isNotEmpty())
            uiState.checkBoxList.addAll(List(allExpense.value.size) { false })
        delay(500)
        uiState.showExpenseList.value = true
    }
    LaunchedEffect(key1 = uiState.totalSelectedItem.value) {
        if (allExpense.value.isNotEmpty())
            uiState.selectAll.value = uiState.totalSelectedItem.value == allExpense.value.size
    }

    LoadingDialog(loadingState = uiState.loadingState)


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
            if (!uiState.showExpenseList.value) {
                var rotation by remember { mutableFloatStateOf(0f) }
                val infiniteTransition = rememberInfiniteTransition(label = "")
                rotation = infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 1000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ), label = ""
                ).value

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_sync),
                            contentDescription = "Rotating Icon",
                            tint = light_enabled_color,
                            modifier = Modifier
                                .size(48.dp)
                                .graphicsLayer {
                                    rotationZ = rotation
                                }
                        )

                        Text(
                            text = "Syncing.....",
                            textAlign = TextAlign.Center,
                            style = Typography.bodyMedium.copy(color = onBackground)
                        )
                    }
                }
            } else {
                Crossfade(
                    targetState = allExpense.value.isNotEmpty(),
                    label = "animation crossfade",
                ) {
                    when (it) {
                        true -> {
                            val selectAllInteractionSource = remember { MutableInteractionSource() }
                            val animateShowList = remember { mutableStateOf(false) }
                            val screenHeight = LocalConfiguration.current.screenHeightDp
                            LaunchedEffect(key1 = Unit) {
                                delay(100)
                                animateShowList.value = true
                            }
                            Column {

                                ExpenseItemDeleteDialog(
                                    showDialog = uiState.showDeleteDialog,
                                    onYes = {
                                        viewModel.deleteExpenses(
                                            allExpense.value.map {
                                                it.toExpense()
                                            }
                                        )
                                        uiState.showDeleteDialog.value = false
                                    },
                                    onNo = { uiState.showDeleteDialog.value = false }
                                )

                                launch {
                                    selectAllInteractionSource.interactions.collectLatest {
                                        when (it) {
                                            is PressInteraction.Release -> {
                                                uiState.totalSelectedItem.value =
                                                    if (uiState.selectAll.value) allExpense.value.size else 0
                                                launch {

                                                    for (i in 0..<uiState.checkBoxList.size) {
                                                        uiState.checkBoxList[i] =
                                                            uiState.selectAll.value
                                                    }
                                                }
                                            }

                                            else -> {}
                                        }
                                    }
                                }

                                Column {
                                    AnimatedVisibility(
                                        visible = uiState.showSelectCheckbox.value, enter = fadeIn(
                                            tween(tweenAnimDuration)
                                        ), exit = fadeOut(
                                            tween(tweenAnimDuration)
                                        )
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = generalPadding),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {

                                            RadioButton(
                                                selected = uiState.selectAll.value,
                                                onClick = {
                                                    uiState.selectAll.value =
                                                        !uiState.selectAll.value
                                                },
                                                interactionSource = selectAllInteractionSource,
                                                colors = RadioButtonDefaults.colors().copy(
                                                    selectedColor = light_enabled_color
                                                )
                                            )

                                            Text(
                                                text = if (uiState.selectAll.value) "Deselect All" else "Select All",
                                                style = Typography.bodyMedium.copy(color = onBackground)
                                            )

                                            Spacer(modifier = Modifier.weight(1f))
                                            IconButton(
                                                onClick = {
                                                    uiState.showDeleteDialog.value = true
                                                }
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.ic_bin),
                                                    contentDescription = "delete icon",
                                                    tint = light_enabled_color
                                                )
                                            }


                                            IconButton(
                                                onClick = {
                                                    uiState.showSelectCheckbox.value = false
                                                    uiState.selectAll.value = false
                                                    uiState.totalSelectedItem.value = 0
                                                    launch {
                                                        for (i in 0..<uiState.checkBoxList.size) {
                                                            uiState.checkBoxList[i] =
                                                                uiState.selectAll.value

                                                        }
                                                    }
                                                }
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.ic_close),
                                                    contentDescription = "delete icon",
                                                    tint = light_enabled_color
                                                )
                                            }
                                        }
                                    }

                                    HorizontalLineOnBackground()

                                    AnimatedVisibility(
                                        visible = animateShowList.value,
                                        enter = slideInVertically(tween(tweenAnimDuration - 100)) { it + screenHeight },
                                        exit = slideOutVertically(tween(tweenAnimDuration)) { screenHeight }
                                    ) {
                                        LazyColumn(
                                            modifier = Modifier
                                                .padding(horizontal = generalPadding)
                                        ) {

                                            items(
                                                allExpense.value.size,
                                                key = { allExpense.value[it].id }) { ind ->
                                                val interactionSource =
                                                    remember { MutableInteractionSource() }
                                                val longPressed =
                                                    interactionSource.collectIsPressedAsState()
                                                if (longPressed.value) {
                                                    uiState.showSelectCheckbox.value = true
                                                    uiState.checkBoxList.apply {
                                                        this[ind] = true
                                                    }
                                                }
                                                Row(
                                                    modifier = Modifier
                                                        .padding(vertical = halfGeneralPadding)
                                                        .generalBorder()
                                                        .clickable(
                                                            interactionSource = interactionSource,
                                                            indication = LocalIndication.current,
                                                            onClick = {
                                                                if (!uiState.showSelectCheckbox.value) {
                                                                    coroutineScope.launch {
                                                                        appState.navigate(
                                                                            Screen.ExpenseDetailScreen.getRoute(
                                                                                allExpense.value[ind].toExpense()
                                                                            )
                                                                        )
                                                                    }
                                                                } else {
                                                                    if (!longPressed.value)
                                                                        uiState.checkBoxList
                                                                            .also {
                                                                                it[ind] = !it[ind]
                                                                                uiState.totalSelectedItem.value += if (it[ind]) 1 else -1
                                                                            }
                                                                }
                                                            }
                                                        )
                                                        .background(surface)
                                                        .padding(vertical = generalPadding)
                                                        .padding(end = generalPadding),
                                                    verticalAlignment = Alignment.CenterVertically

                                                ) {

                                                    AnimatedVisibility(
                                                        visible = uiState.showSelectCheckbox.value
                                                    ) {
                                                        RadioButton(
                                                            selected = uiState.checkBoxList[ind],
                                                            onClick = {
                                                                uiState.checkBoxList.also {
                                                                    it[ind] = !it[ind]
                                                                }
                                                                uiState.totalSelectedItem.value += if (uiState.checkBoxList[ind]) 1 else -1
                                                            },
                                                            colors = RadioButtonDefaults.colors()
                                                                .copy(
                                                                    selectedColor = light_enabled_color,
                                                                    unselectedColor = onSurface
                                                                )
                                                        )
                                                    }

                                                    Column(
                                                        modifier = Modifier
                                                            .weight(1f)
                                                            .padding(start = if (uiState.showSelectCheckbox.value) 0.dp else generalPadding)
                                                    ) {
                                                        Row(
                                                            modifier = Modifier
                                                                .fillMaxWidth(),
                                                            horizontalArrangement = Arrangement.SpaceBetween
                                                        ) {
                                                            Text(
                                                                text = allExpense.value[ind].title,
                                                                style = Typography.bodyLarge.copy(
                                                                    color = onSurface
                                                                ),
                                                                modifier = Modifier
                                                            )
                                                            if (allExpense.value[ind].operation != null && allExpense.value[ind].operation?.isNotEmpty()!!) {
                                                                Icon(
                                                                    painter = painterResource(id = R.drawable.ic_sync),
                                                                    contentDescription = "sync icon",
                                                                    modifier = Modifier
                                                                        .size(20.dp),
                                                                    tint = enabled_color
                                                                )
                                                            }
                                                        }

                                                        Row(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .padding(vertical = generalPadding),
                                                            verticalAlignment = Alignment.CenterVertically,
                                                            horizontalArrangement = Arrangement.SpaceBetween
                                                        ) {
                                                            Text(
                                                                text = "items",
                                                                style = Typography.bodyMedium.copy(
                                                                    color = onSurface
                                                                )
                                                            )

                                                            Text(
                                                                text = allExpense.value[ind].items.size.toString(),
                                                                style = Typography.bodyMedium.copy(
                                                                    color = onSurface
                                                                )
                                                            )
                                                        }

                                                        Row(
                                                            modifier = Modifier
                                                                .fillMaxWidth(),
                                                            horizontalArrangement = Arrangement.SpaceBetween
                                                        ) {

                                                            Text(
                                                                text = dateString(allExpense.value[ind].date),
                                                                style = Typography.bodyMedium.copy(
                                                                    color = onSurface
                                                                )
                                                            )

                                                            Text(
                                                                text = getRupeeString(allExpense.value[ind].totalAmount),
                                                                style = Typography.bodyLarge.copy(
                                                                    color = onSurface
                                                                )
                                                            )

                                                        }
                                                    }

                                                }

                                            }

                                        }
                                    }
                                }
                            }
                        }

                        false -> {
                            val lottieComposition by rememberLottieComposition(
                                LottieCompositionSpec.RawRes(
                                    R.raw.empty_list
                                )
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

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
                        }
                    }
                }
            }
        }
    }

}

private fun dateString(date: Date): String {
    val formatter = SimpleDateFormat(date_formatter_pattern_without_time, java.util.Locale.UK)
    return formatter.format(date)
}