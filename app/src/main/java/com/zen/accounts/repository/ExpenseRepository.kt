package com.zen.accounts.repository

import com.zen.accounts.api.ExpenseApi
import com.zen.accounts.api.resource.Resource
import com.zen.accounts.api.resource.Response
import com.zen.accounts.db.dao.ExpenseDao
import com.zen.accounts.db.model.Expense
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val addExpenseDao: ExpenseDao,
    private val expenseApi: ExpenseApi
) {

    val allExpense : Flow<List<Expense>> = addExpenseDao.getAllExpenses()
    private val _allExpenseFromApi : MutableStateFlow<List<Expense>> = MutableStateFlow(listOf())
    val allExpenseFromApi : StateFlow<List<Expense>> get() = _allExpenseFromApi
    suspend fun insertExpenseIntoRoom(expense: Expense) {
        addExpenseDao.insertExpense(expense)
    }

    suspend fun getAllExpensesFromApi() {
        CoroutineScope(Dispatchers.IO).launch {
            _allExpenseFromApi.emit(expenseApi.getAllExpenseFromWeb().value)
        }
    }
}