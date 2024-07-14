package com.zen.accounts.ui.viewmodels

import android.app.Application
import android.content.Context
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zen.accounts.R
import com.zen.accounts.api.resource.Resource
import com.zen.accounts.db.datastore.UserDataStore
import com.zen.accounts.db.model.User
import com.zen.accounts.repository.AuthRepository
import com.zen.accounts.ui.screens.auth.register.RegisterUiState
import com.zen.accounts.ui.screens.auth.register.RegisterUiStateHolder
import com.zen.accounts.ui.screens.common.LoadingState
import com.zen.accounts.ui.screens.common.empty_email
import com.zen.accounts.ui.screens.common.empty_pass
import com.zen.accounts.ui.screens.common.success_register
import com.zen.accounts.utility.Utility
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewmodel() {

    val registerUiState by lazy { RegisterUiState() }

    val registerUiStateHolder by lazy { RegisterUiStateHolder() }

    fun showSnackBar() {
        super.showSnackBar(registerUiStateHolder.showSnackBar) { registerUiStateHolder.updateState(showSnackBar = it) }
    }

    fun registerUser(user: User, pass: String, dataStore: UserDataStore) {
        viewModelScope.launch {
            registerUiState.apply {
                loadingState.value = LoadingState.LOADING
                when (val res = authRepository.registerUser(user, pass)) {
                    is Resource.SUCCESS -> {
                        loadingState.value = LoadingState.SUCCESS
                        snackBarText.value = success_register
                        userName.value = ""
                        email.value = ""
                        phone.value = ""
                        password.value = ""
                    }

                    is Resource.FAILURE -> {
                        snackBarText.value = res.message
                        loadingState.value = LoadingState.FAILURE
                        userName.value = ""
                        email.value = ""
                        phone.value = ""
                        password.value = ""
                    }
                }
            }
        }
    }
}