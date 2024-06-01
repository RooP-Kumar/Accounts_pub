package com.zen.accounts.repository

import android.content.Context
import com.zen.accounts.db.datastore.UserDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

open class BaseRepository @Inject constructor() {
    @Inject
    @ApplicationContext
    lateinit var context: Context

    val dataStore : UserDataStore by lazy { UserDataStore(context) }
}