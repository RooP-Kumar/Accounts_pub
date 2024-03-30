package com.zen.accounts.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zen.accounts.db.dao.ExpenseDao
import com.zen.accounts.db.model.Expense
import com.zen.accounts.db.model.ExpenseItem
import com.zen.accounts.db.model.User

@Database([Expense::class, ExpenseItem::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getExpenseDao() : ExpenseDao
}