package com.zen.accounts.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.zen.accounts.db.datastore.UserDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
open class BaseViewmodel @Inject constructor(): ViewModel() {
    @Inject
    @ApplicationContext
    lateinit var context : Context

    val dataStore : UserDataStore by lazy { UserDataStore(context) }
}