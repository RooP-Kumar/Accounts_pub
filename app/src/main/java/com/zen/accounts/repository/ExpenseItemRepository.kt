package com.zen.accounts.repository

import com.zen.accounts.db.dao.ExpenseItemDao
import com.zen.accounts.db.model.ExpenseItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExpenseItemRepository @Inject constructor(
    private val expenseItemDao: ExpenseItemDao
) {

    val allExpenseItem : Flow<List<ExpenseItem>> = expenseItemDao.getAllExpenseItems()


    suspend fun insertExpenseItemIntoRoom(expenseItem: ExpenseItem) {
        expenseItemDao.insertExpenseItem(expenseItem)
    }

    suspend fun updateExpenseItem(expenseItem: ExpenseItem) {
        expenseItemDao.updateExpenseItem(expenseItem)
    }

    suspend fun deleteExpenseItem(id: Long) {
        expenseItemDao.deleteExpenseItem(id)
    }

    suspend fun clearExpenseItemTable() {
        expenseItemDao.deleteAllItems()
    }
}