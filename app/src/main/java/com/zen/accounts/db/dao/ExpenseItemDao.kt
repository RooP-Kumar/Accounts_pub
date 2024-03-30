package com.zen.accounts.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zen.accounts.db.model.Expense
import com.zen.accounts.db.model.ExpenseItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExpenseItem(expenseItem : ExpenseItem)

    @Query("SELECT * FROM expense_items WHERE id = :id")
    fun getExpenseItem(id : Long) : Flow<ExpenseItem>

    @Query("SELECT * FROM expense_items")
    fun getAllExpenseItems() : Flow<List<ExpenseItem>>
}