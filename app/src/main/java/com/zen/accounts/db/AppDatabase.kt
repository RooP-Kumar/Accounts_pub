package com.zen.accounts.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zen.accounts.db.dao.BackupTrackerDao
import com.zen.accounts.db.dao.ExpenseDao
import com.zen.accounts.db.dao.ExpenseItemDao
import com.zen.accounts.db.model.BackupTracker
import com.zen.accounts.db.model.Expense
import com.zen.accounts.db.model.ExpenseItem

@Database([Expense::class, ExpenseItem::class, BackupTracker::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getExpenseDao() : ExpenseDao
    abstract fun getExpenseItemDao() : ExpenseItemDao
    abstract fun getBackupTrackerDao() : BackupTrackerDao
}