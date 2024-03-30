package com.zen.accounts.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zen.accounts.db.model.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExpense(expense : Expense)

    @Query("SELECT * FROM expenses WHERE id = :id")
    fun getExpense(id : Long) : Flow<Expense>

    @Query("SELECT * FROM expenses")
    fun getAllExpenses() : Flow<List<Expense>>
}