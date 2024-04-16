package com.zen.accounts.ui.screens.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.window.Dialog
import com.zen.accounts.ui.theme.background
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.halfGeneralPadding
import com.zen.accounts.utility.enums.LoadingState

@Composable
fun LoadingDialog(
    loadingState: MutableState<LoadingState>
) {
    val showDialog : MutableState<Boolean> = remember { mutableStateOf(false) }
    LaunchedEffect(key1 = loadingState.value) {
        when(loadingState.value) {
            LoadingState.LOADING -> {
                showDialog.value = true
            }
            else -> {showDialog.value = false}
        }
    }

    AnimatedVisibility(visible = showDialog.value) {
        Dialog(
            onDismissRequest = {}
        ) {
            Column(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(generalPadding))
                    .background(background)
                    .padding(generalPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(halfGeneralPadding))
                Text(
                    text = "Loading....."
                )
            }
        }
    }

}