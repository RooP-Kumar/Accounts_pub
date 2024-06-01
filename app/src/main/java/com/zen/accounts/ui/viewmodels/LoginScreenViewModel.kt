package com.zen.accounts.ui.viewmodels

import androidx.lifecycle.viewModelScope
import com.zen.accounts.api.resource.Resource
import com.zen.accounts.repository.AuthRepository
import com.zen.accounts.repository.ExpenseRepository
import com.zen.accounts.ui.screens.auth.login.LoginUiState
import com.zen.accounts.ui.screens.common.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val expenseRepository: ExpenseRepository
): BaseViewmodel() {
    val loginUiState by lazy { LoginUiState() }

    fun loginUser(email: String, pass : String) {
        viewModelScope.launch(Dispatchers.IO) {
            loginUiState.apply {
                loadingState.value = LoadingState.LOADING
                when(val res = authRepository.loginUser(email, pass)) {
                    is Resource.SUCCESS -> {
                        snackBarText.value = "You have successfully login to your account."
                        emailUsernamePhone.value = ""
                        password.value = ""
                        dataStore.run { saveUser(res.value.value) }
                        when(expenseRepository.getExpensesFromFirebase(res.value.value.uid)) {
                            is Resource.SUCCESS -> {
                                loadingState.value = LoadingState.SUCCESS
                            }
                            is Resource.FAILURE -> {
                                loadingState.value = LoadingState.FAILURE
                            }
                        }

                    }
                    is Resource.FAILURE -> {
                        loadingState.value = LoadingState.FAILURE
                        snackBarText.value = res.message
                        emailUsernamePhone.value = ""
                        password.value = ""
                    }
                }

            }
        }
    }
}