package com.zen.accounts.utility

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.zen.accounts.db.model.ExpenseItem
import com.zen.accounts.ui.theme.Typography
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.getRupeeString
import com.zen.accounts.ui.theme.halfGeneralPadding
import com.zen.accounts.ui.theme.normalPadding
import com.zen.accounts.ui.theme.onBackground
import com.zen.accounts.ui.theme.onSurface
import androidx.compose.ui.text.TextStyle

@Composable
fun ExpenseItemLayout(
    expenseItem: ExpenseItem,
    amountTextWidth : MutableState<Dp>,
    textStyle: TextStyle = Typography.bodyLarge.copy(color = onBackground),
    qtyTextStyle: TextStyle = Typography.bodyMedium.copy(color = onBackground)
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
                text = expenseItem.itemName,
                style = textStyle
            )

            Text(
                text = getQtyInString(expenseItem.itemQty),
                style = qtyTextStyle,
                modifier = Modifier.padding(start = normalPadding)
            )
        }

        Text(
            text = getRupeeString(expenseItem.itemPrice),
            style = textStyle,
            modifier = Modifier
                .wrapContentWidth()
                .width(amountTextWidth.value)
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