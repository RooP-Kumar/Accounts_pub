package com.zen.accounts.db.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.zen.accounts.db.model.User
import com.zen.accounts.ui.screens.common.BackupPlan
import com.zen.accounts.ui.screens.common.backup_plan
import com.zen.accounts.ui.screens.common.datastore_name
import com.zen.accounts.ui.screens.common.profile_pic
import com.zen.accounts.ui.screens.common.system_in_dark_mode
import com.zen.accounts.ui.screens.common.user_data_store_key
import com.zen.accounts.utility.backupPlanToString
import com.zen.accounts.utility.stringToBackupPlan
import com.zen.accounts.utility.stringToUser
import com.zen.accounts.utility.userToString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UserDataStore(private val context : Context) {
    companion object {
        private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(
            datastore_name
        )
        val USER_DATA_STORE_KEY = stringPreferencesKey(user_data_store_key)
        val SYSTEM_IN_DARK_MODE = booleanPreferencesKey(system_in_dark_mode)
        val BACKUP_PLAN = stringPreferencesKey(backup_plan)
        val PROFILE_PIC = byteArrayPreferencesKey(profile_pic)
    }

    val  getUser : Flow<User?> = context.dataStore.data
        .map { preferences ->
            val userString = preferences[USER_DATA_STORE_KEY]
            if (userString != null) stringToUser(userString) else null
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

    suspend fun getUser() : User? {
        return withContext(Dispatchers.IO) {
            val preferences = context.dataStore.data.firstOrNull()
            if (preferences != null) {
                val userString = preferences[USER_DATA_STORE_KEY]
                if (userString != null) stringToUser(userString) else null
            } else null
        }
    }

    suspend fun saveUser(user : User) {
        context.dataStore.edit { preferences ->
            preferences[USER_DATA_STORE_KEY] = userToString(user) // profilePic = null because gonna remove this property in future. so that already existing user's app should not crash.
        }
    }

    suspend fun saveProfilePic(image: ByteArray) {
        context.dataStore.edit {preferences ->
            preferences[PROFILE_PIC] = image
        }
    }

    suspend fun getProfilePic() : ByteArray? {
        return withContext(Dispatchers.IO) {
            val preferences = context.dataStore.data.firstOrNull()
            if (preferences != null) {
                preferences[PROFILE_PIC]
            } else null
        }
    }

    suspend fun removeProfilePic() {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { pref ->
                pref[PROFILE_PIC] = ByteArray(0)
            }
        }
    }

    val getProfilePic: Flow<ByteArray?> = context.dataStore.data
        .map { pref ->
            val image = pref[PROFILE_PIC]
            if((image?.size ?: 0) == 0){
                null
            } else {
                image
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
                preferences[USER_DATA_STORE_KEY] = userToString(User())
            }
        }
        removeProfilePic()
    }

}