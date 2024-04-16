package com.zen.accounts.ui.screens.auth.register

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterScreenViewModel @Inject constructor(): ViewModel() {
    val registerUiState by lazy { RegisterUiState() }
}