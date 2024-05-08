package com.zen.accounts.repository

import android.util.Log
import com.zen.accounts.api.ExpenseApi
import com.zen.accounts.api.resource.Resource
import com.zen.accounts.api.resource.Response
import com.zen.accounts.db.dao.BackupTrackerDao
import com.zen.accounts.db.dao.ExpenseDao
import com.zen.accounts.db.model.BackupTracker
import com.zen.accounts.db.model.Expense
import com.zen.accounts.states.getAppState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val addExpenseDao: ExpenseDao,
    private val backupTrackerDao: BackupTrackerDao,
    private val expenseApi: ExpenseApi
) {

    val allExpense : Flow<List<Expense>> = addExpenseDao.getAllExpenses()
    suspend fun insertExpenseIntoRoom(expense: Expense) {
        addExpenseDao.insertExpense(expense)
        backupTrackerDao.insertBackupTracker(
            BackupTracker(
                expenseId = expense.id,
                date = Date(System.currentTimeMillis())
            )
        )
    }

    suspend fun clearExpenseTable() {
        addExpenseDao.clearTable()
    }

    suspend fun getExpensesFromFirebase(uid: String) : Resource<Unit> {
        return withContext(Dispatchers.IO) {
            val res = expenseApi.getExpenseFromFirebase(uid)
            if (res.status) {
                addExpenseDao.insertExpenseList(res.value)
                Resource.SUCCESS(Unit)
            } else {
                Resource.FAILURE(res.message)
            }
        }
    }

    suspend fun uploadExpenseToFirebase(uid : String) : Resource<Response<Unit>> {
        val expenseList = addExpenseDao.getAllNotBackupExpense()
        if(expenseList.isEmpty()) {
            return Resource.FAILURE("Expense are already backup")
        }
        return withContext(Dispatchers.IO) {
            val res = expenseApi.uploadToFirebase(uid, expenseList)
            if(res.status) Resource.SUCCESS(res)
            else Resource.FAILURE(res.message)
        }
    }

    suspend fun clearBackupTable() {
        backupTrackerDao.deleteAll()
    }

    suspend fun isBackupTableEmpty() : Boolean {
        return backupTrackerDao.getTableEntryCount() == 0L
    }


}