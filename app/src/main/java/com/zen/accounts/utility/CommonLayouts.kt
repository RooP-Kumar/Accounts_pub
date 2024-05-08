package com.zen.accounts.utility

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.zen.accounts.db.model.ExpenseItem
import com.zen.accounts.ui.screens.common.getRupeeString
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.onBackground

@Composable
fun ExpenseItemLayout(
    expenseItem: ExpenseItem,
    textStyle: TextStyle = Typography.bodyLarge.copy(color = onBackground)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = generalPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = expenseItem.itemTitle,
                style = textStyle
            )
        }

        Text(
            text = getRupeeString(expenseItem.itemAmount ?: 0.0),
            style = textStyle,
            modifier = Modifier
                .wrapContentWidth()
        )
    }
}

// Function to Get Quantity
fun getQtyInString(qty : Int) : String {
    if(qty == 0) {
        return ""
    }
    return "qty x $qty"
}