package com.zen.accounts.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.zen.accounts.db.model.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertExpense(expense : Expense)

    @Insert
    suspend fun insertExpenseList(expenses: List<Expense>)

    @Query("DELETE FROM expenses")
    suspend fun clearTable()

    @Query("SELECT * FROM expenses WHERE id = :id")
    fun getExpense(id : Long) : Flow<Expense>

    @Query("SELECT * FROM expenses")
    fun getAllExpenses() : Flow<List<Expense>>

    @Query("SELECT e.id, e.title, e.items, e.totalAmount, e.date  FROM expenses e INNER JOIN backup_tracker b on e.id = b.expenseId")
    fun getAllNotBackupExpense() : List<Expense>
}