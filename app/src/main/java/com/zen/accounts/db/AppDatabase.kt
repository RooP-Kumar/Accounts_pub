package com.zen.accounts.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zen.accounts.db.dao.ExpenseDao
import com.zen.accounts.db.dao.ExpenseItemDao
import com.zen.accounts.db.dao.ExpenseTypeDao
import com.zen.accounts.db.model.Expense
import com.zen.accounts.db.model.ExpenseItem
import com.zen.accounts.db.model.ExpenseType
import com.zen.accounts.db.model.User

@Database([Expense::class, ExpenseItem::class, ExpenseType::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getExpenseDao() : ExpenseDao
    abstract fun getExpenseTypeDao() : ExpenseTypeDao
    abstract fun getExpenseItemDao() : ExpenseItemDao
}