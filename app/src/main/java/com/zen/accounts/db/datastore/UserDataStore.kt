package com.zen.accounts.db.datastore

import android.content.Context
import android.service.autofill.UserData
import androidx.core.os.bundleOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.zen.accounts.db.model.User
import com.zen.accounts.ui.screens.common.*
import com.zen.accounts.utility.backupPlanToString
import com.zen.accounts.utility.stringToBackupPlan
import com.zen.accounts.utility.stringToUser
import com.zen.accounts.utility.userToString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserDataStore(private val context : Context) {
    companion object {
        private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(
            datastore_name
        )
        val USER_DATA_STORE_KEY = stringPreferencesKey(user_data_store_key)
        val SYSTEM_IN_DARK_MODE = booleanPreferencesKey(system_in_dark_mode)
        val BACKUP_PLAN = stringPreferencesKey(backup_plan)
    }

    val  getUser : Flow<User?> = context.dataStore.data
        .map { preferences ->
            val userString = preferences[USER_DATA_STORE_KEY]
            if (userString != null) stringToUser(userString) else null
        }

    suspend fun getUser() : User? {
        return withContext(Dispatchers.IO) {
            val preferences = context.dataStore.data.firstOrNull()
            if (preferences != null) {
                val userString = preferences[USER_DATA_STORE_KEY]
                if (userString != null) stringToUser(userString) else null
            } else null
        }
    }

    val isInDarkMode : Flow<Boolean?> = context.dataStore.data.map {
        it[SYSTEM_IN_DARK_MODE]
    }

    suspend fun updateBackupPlan(backupPlan: BackupPlan) {
        withContext(Dispatchers.IO) {
            context.dataStore.edit {
                it[BACKUP_PLAN] = backupPlanToString(backupPlan)
            }
        }
    }
    suspend fun getBackupPlan() : BackupPlan {
        return withContext(Dispatchers.IO) {
            val preferences = context.dataStore.data.firstOrNull()
            if (preferences != null) {
                val backupPlanString = preferences[BACKUP_PLAN]
                if(backupPlanString != null)
                    stringToBackupPlan(backupPlanString)
                else
                    BackupPlan.Off
            } else BackupPlan.Off
        }
    }

    suspend fun saveUser(user : User) {
        context.dataStore.edit { preferences ->
            preferences[USER_DATA_STORE_KEY] = userToString(user)
        }
    }

    suspend fun saveIsDarkMode(data : Boolean) {
        context.dataStore.edit {preferences ->
            preferences[SYSTEM_IN_DARK_MODE] = data
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