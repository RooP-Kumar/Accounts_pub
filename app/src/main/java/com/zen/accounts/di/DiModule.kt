package com.zen.accounts.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.zen.accounts.api.retrofit.ExpenseService
import com.zen.accounts.db.AppDatabase
import com.zen.accounts.db.dao.BackupTrackerDao
import com.zen.accounts.db.dao.ExpenseDao
import com.zen.accounts.db.dao.ExpenseItemDao
import com.zen.accounts.utility.DateDeSerializerForApi
import com.zen.accounts.utility.DateSerializerForApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date

@Module
@InstallIn(SingletonComponent::class)
class DiModule {
    @Provides
    fun getDatabase(@ApplicationContext context : Context) : AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "App Database",
        ).build()
    }

    @Provides
    fun getExpenseDao(db : AppDatabase) : ExpenseDao {
        return db.getExpenseDao()
    }

    @Provides
    fun getExpenseItemDao(db : AppDatabase) : ExpenseItemDao {
        return db.getExpenseItemDao()
    }

    @Provides
    fun getBackupTrackerDao(db : AppDatabase) : BackupTrackerDao {
        return db.getBackupTrackerDao()
    }

    @Provides
    fun getGson() : Gson{
        return GsonBuilder()
            .serializeNulls()
            .serializeSpecialFloatingPointValues()
            .registerTypeAdapter(Date::class.java, DateSerializerForApi())
            .registerTypeAdapter(Date::class.java, DateDeSerializerForApi())
            .create()
    }

    @Provides
    fun getRetrofit(gson : Gson) : Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.31.14:9090")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    fun getExpenseService(retrofit : Retrofit) : ExpenseService {
        return retrofit.create(ExpenseService::class.java)
    }
}