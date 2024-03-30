package com.zen.accounts.db.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.zen.accounts.db.model.User
import com.zen.accounts.ui.theme.datastore_name
import com.zen.accounts.ui.theme.user_data_store_key
import com.zen.accounts.utility.stringToUser
import com.zen.accounts.utility.userToString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserDataStore(private val context : Context) {
    companion object {
        private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(datastore_name)
        val USER_DATA_STORE_KEY = stringPreferencesKey(user_data_store_key)
    }

    val  getUser : Flow<User?> = context.dataStore.data
        .map { preferences ->
            val userString = preferences[USER_DATA_STORE_KEY]
            if (userString != null) stringToUser(userString) else null
        }

    suspend fun saveUser(user : User) {
        context.dataStore.edit { preferences ->
            preferences[USER_DATA_STORE_KEY] = userToString(user)
        }
    }

    suspend fun logoutUser() {
        context.dataStore.edit { preferences ->
            val userString = preferences[USER_DATA_STORE_KEY]
            if(userString != null) {
                val user = stringToUser(userString)
                user.isAuthenticated = false
                preferences[USER_DATA_STORE_KEY] = userToString(user)
            }
        }
    }

}