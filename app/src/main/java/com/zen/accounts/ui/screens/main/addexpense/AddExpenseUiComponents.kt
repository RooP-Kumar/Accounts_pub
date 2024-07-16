package com.zen.accounts.ui.screens.main.addexpense

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zen.accounts.states.AppState
import com.zen.accounts.ui.screens.common.GeneralEditText
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.halfGeneralPadding
import com.zen.accounts.ui.theme.primary_color

@Composable
fun UpperTitleSection(
    appState: AppState,
    title : MutableState<String>
) {
    Text(
        text = "Add Title",
        style = Typography.titleSmall.copy(color = MaterialTheme.colorScheme.onBackground),
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
            style = Typography.titleSmall.copy(color = MaterialTheme.colorScheme.onBackground),
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
            .background(primary_color)
    )
}
