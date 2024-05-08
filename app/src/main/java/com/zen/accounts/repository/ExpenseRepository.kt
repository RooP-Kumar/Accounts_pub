package com.zen.accounts.ui.screens.main.addexpense

import com.zen.accounts.api.ExpenseApi
import com.zen.accounts.api.resource.Resource
import com.zen.accounts.api.resource.Response
import com.zen.accounts.db.dao.BackupTrackerDao
import com.zen.accounts.db.dao.ExpenseDao
import com.zen.accounts.db.model.BackupTracker
import com.zen.accounts.db.model.Expense
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val addExpenseDao: ExpenseDao,
    private val backupTrackerDao: BackupTrackerDao
    private val expenseApi: ExpenseApi
) {

    val allExpense : Flow<List<Expense>> = addExpenseDao.getAllExpenses()
    suspend fun insertExpenseIntoRoom(expense: Expense) {
        addExpenseDao.insertExpense(expense)
        backupTrackerDao.insertBackupTracker(
            BackupTracker(
                
            )
        )
    }

    suspend fun getExpensesFromFirebase() : Resource<Response<List<Expense>>> {
        return withContext(Dispatchers.IO) {
            val res = expenseApi.getExpenseFromFirebase("")
            if(res.status) Resource.SUCCESS(res)
            else Resource.FAILURE(res.message)
        }
    }

    suspend fun uploadExpenseToFirebase(expense: Expense) : Resource<Response<Unit>> {
        return withContext(Dispatchers.IO) {
            val res = expenseApi.uploadToFirebase("", expense)
            if(res.status) Resource.SUCCESS(res)
            else Resource.FAILURE(res.message)
        }
    }
}