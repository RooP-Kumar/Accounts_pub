package com.zen.accounts.ui.screens.main.addexpense

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.zen.accounts.R
import com.zen.accounts.db.model.Expense
import com.zen.accounts.db.model.ExpenseItem
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.navigation.Screen
import com.zen.accounts.ui.screens.common.GeneralButton
import com.zen.accounts.ui.screens.common.GeneralEditText
import com.zen.accounts.ui.screens.common.getRupeeString
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.background
import com.zen.accounts.ui.theme.border_color
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.halfGeneralPadding
import com.zen.accounts.ui.theme.onBackground
import com.zen.accounts.ui.theme.onSurface
import com.zen.accounts.ui.theme.surface
import com.zen.accounts.ui.viewmodels.AddExpenseViewModel
import com.zen.accounts.utility.ExpenseItemLayout
import com.zen.accounts.utility.generalBorder
import java.util.Date

@Composable
fun UpperTitleSection(
    appState: AppState,
    title : MutableState<String>
) {
    Text(
        text = "Add Title",
        style = Typography.titleSmall.copy(color = onBackground),
        modifier = Modifier
            .padding(top = halfGeneralPadding)
            .padding(vertical = halfGeneralPadding, horizontal = generalPadding)
    )

    GeneralEditText(
        text = title.value,
        onValueChange = {title.value = it},
        modifier = Modifier.fillMaxWidth(),
        placeholderText = "Enter Title",
        showClickEffect = false
    )
}

@Composable
fun AddExpenseItemTitleSection() {
    var tempModifier : Modifier
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Add Expense Items",
            style = Typography.titleSmall.copy(color = onBackground),
            modifier = Modifier
                .padding(vertical = halfGeneralPadding, horizontal = generalPadding)
        )
    }

    HorizontalLineOnBackground()
}


@Composable
fun HorizontalLineOnBackground(modifier: Modifier = Modifier) {
    Spacer(
        modifier
            .fillMaxWidth()
            .height(0.2.dp)
            .background(border_color)
    )
}
