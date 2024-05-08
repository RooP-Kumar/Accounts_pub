package com.zen.accounts.repository

import com.zen.accounts.db.dao.ExpenseItemDao
import com.zen.accounts.db.model.ExpenseItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExpenseItemRepository @Inject constructor(
    private val expenseItemDao: ExpenseItemDao
) {
    val allExpenseItem : Flow<List<ExpenseItem>> = expenseItemDao.getAllExpenseItems()

    suspend fun insertExpenseItemIntoRoom(expenseItem: ExpenseItem) {
        expenseItemDao.insertExpenseItem(expenseItem)
    }

    suspend fun clearExpenseItemTable() {
        expenseItemDao.deleteAllItems()
    }
}