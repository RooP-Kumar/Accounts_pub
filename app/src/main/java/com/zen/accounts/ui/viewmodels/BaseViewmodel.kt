package com.zen.accounts.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.zen.accounts.db.datastore.UserDataStore
import com.zen.accounts.utility.main
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
open class BaseViewmodel @Inject constructor(): ViewModel() {
    @Inject
    @ApplicationContext
    lateinit var context : Context

    val dataStore : UserDataStore by lazy { UserDataStore(context) }

    fun showSnackBar(snackBarState: StateFlow<Boolean>, updateSnackBarState: (Boolean?) -> Unit) {
        main {
            if(!snackBarState.value) {
                updateSnackBarState(true)
                delay(2500)
                updateSnackBarState(false)
            }
        }
    }
}