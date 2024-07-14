package com.zen.accounts.ui.screens.auth.register

import com.zen.accounts.ui.screens.common.LoadingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RegisterUiStateHolder {
    private val _userName: MutableStateFlow<String> = MutableStateFlow("")
    val userName : StateFlow<String> get() = _userName

    private val _email: MutableStateFlow<String> = MutableStateFlow("")
    val email: StateFlow<String> get() = _email

    private val _phone: MutableStateFlow<String> = MutableStateFlow("")
    val phone: StateFlow<String> get() = _phone

    private val _password: MutableStateFlow<String> = MutableStateFlow("")
    val password: StateFlow<String> get() = _password

    private val _loadingState: MutableStateFlow<LoadingState> = MutableStateFlow(LoadingState.IDLE)
    val loadingState: StateFlow<LoadingState> get() = _loadingState

    private val _showSnackBar: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showSnackBar: StateFlow<Boolean> get() = _showSnackBar

    private val _snackBarText: MutableStateFlow<String> = MutableStateFlow("")
    val snackBarText: StateFlow<String> get() = _snackBarText

    fun updateState(
        name : String? = null,
        email: String? = null,
        phone : String? = null,
        pass: String? = null,
        loadingState: LoadingState? = null,
        showSnackBar: Boolean? = null,
        snackBarText: String? = null
    ) {
        name?.let { _userName.value = it }
        email?.let { _email.value = it }
        phone?.let { _phone.value = it }
        pass?.let { _password.value = it }
        loadingState?.let { _loadingState.value = it }
        showSnackBar?.let { _showSnackBar.value = it }
        snackBarText?.let { _snackBarText.value = it }
    }
}