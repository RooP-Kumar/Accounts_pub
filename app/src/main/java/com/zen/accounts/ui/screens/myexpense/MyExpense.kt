package com.zen.accounts.ui.screens.myexpense

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.zen.accounts.states.AppState

@Composable
fun MyExpense(
    appState: AppState
) {
    Column {
        Text(text = "MyExpense")
    }
}