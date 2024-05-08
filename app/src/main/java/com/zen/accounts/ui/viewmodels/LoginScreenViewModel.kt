package com.zen.accounts.ui.screens.auth.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zen.accounts.api.resource.Resource
import com.zen.accounts.db.datastore.UserDataStore
import com.zen.accounts.db.model.User
import com.zen.accounts.repository.AuthRepository
import com.zen.accounts.ui.screens.common.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext val context : Context
): ViewModel() {
    val loginUiState by lazy { LoginUiState() }
    private val dataStore = UserDataStore(context = context)

    fun loginUser(uid: String, email: String, pass : String) {
        viewModelScope.launch(Dispatchers.IO) {
            loginUiState.apply {
                loadingState.value = LoadingState.LOADING
                when(val res = authRepository.loginUser(uid, email, pass)) {
                    is Resource.SUCCESS -> {
                        loadingState.value = LoadingState.SUCCESS
                        snackBarText.value = "You have successfully login to your account."
                        emailUsernamePhone.value = ""
                        password.value = ""
                        val tempUser = dataStore.getUser()
                        if(tempUser != null) {
                            dataStore.saveUser(tempUser.copy(uid = uid, isAuthenticated = true))
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