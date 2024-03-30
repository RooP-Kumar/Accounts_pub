package com.zen.accounts.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zen.accounts.db.model.Expense
import com.zen.accounts.db.model.ExpenseType
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseTypeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExpenseType(type : ExpenseType)

    @Query("SELECT * FROM expense_type WHERE id = :id")
    fun getExpenseType(id : Long) : Flow<ExpenseType>

    @Query("SELECT * FROM expense_type")
    fun getAllExpenseType() : Flow<List<ExpenseType>>
}