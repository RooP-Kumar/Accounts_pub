package com.zen.accounts.di

import android.content.Context
import androidx.room.Room
import com.zen.accounts.db.AppDatabase
import com.zen.accounts.db.dao.ExpenseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

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

    // This is the main branch code just for checking.

    @Provides
    fun getExpenseDao(db : AppDatabase) : ExpenseDao {
        return db.getExpenseDao()
    }
}