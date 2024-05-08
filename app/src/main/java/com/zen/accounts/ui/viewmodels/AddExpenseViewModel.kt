package com.zen.accounts.ui.viewmodels

import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import com.zen.accounts.db.model.Expense
import com.zen.accounts.db.model.ExpenseItem
import com.zen.accounts.repository.ExpenseRepository
import com.zen.accounts.ui.screens.common.LoadingState
import com.zen.accounts.ui.screens.main.addexpenseitem.AddExpenseItemUiState
import com.zen.accounts.repository.ExpenseItemRepository
import com.zen.accounts.repository.WorkerRepository
import com.zen.accounts.ui.screens.common.daily_work_request_tag
import com.zen.accounts.ui.screens.common.monthly_work_request_tag
import com.zen.accounts.ui.screens.common.single_work_request_tag
import com.zen.accounts.ui.screens.common.weekly_work_request_tag
import com.zen.accounts.ui.screens.main.addexpense.AddExpenseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val expenseItemRepository: ExpenseItemRepository,
    private val workerRepository: WorkerRepository
) : BaseViewmodel() {
    val addExpenseItemUiState by lazy { AddExpenseItemUiState() }
    val addExpenseUiState by lazy { AddExpenseUiState() }
    val allExpenseItem: Flow<List<ExpenseItem>> = expenseItemRepository.allExpenseItem

    private val _uploadExpenseWorkerInfo : MutableStateFlow<List<WorkInfo>?> = MutableStateFlow(listOf())
    val uploadExpenseWorkerInfo : Flow<List<WorkInfo>?> get() = _uploadExpenseWorkerInfo

    fun addExpenseIntoLocalDatabase(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            addExpenseUiState.loadingState.value = LoadingState.LOADING
            expenseRepository.insertExpenseIntoRoom(expense)
            delay(600)
            addExpenseUiState.loadingState.value = LoadingState.SUCCESS

        }
    }

    fun addExpenseItemIntoLocalDatabase(expenseItem: ExpenseItem) {
        viewModelScope.launch(Dispatchers.IO) {
            addExpenseItemUiState.loadingState.value = LoadingState.LOADING
            expenseItemRepository.insertExpenseItemIntoRoom(expenseItem)
            delay(600)
            addExpenseItemUiState.loadingState.value = LoadingState.SUCCESS
        }
    }

    fun deleteExpenseItemsFromLocalDatabase() {
        viewModelScope.launch {
            expenseItemRepository.clearExpenseItemTable()
        }
    }

    fun startSingleUploadRequest() {
        viewModelScope.launch {
            dataStore.getUser()?.let { user ->
                workerRepository.startUploadingNow(user.uid, single_work_request_tag)
                val tempFlow = workerRepository.getWorkInfoByTag(single_work_request_tag)
                tempFlow.collectLatest {
                    _uploadExpenseWorkerInfo.emit(it)
                }
            }
        }
    }

    fun startDailyUploadRequest() {
        viewModelScope.launch {
            dataStore.getUser()?.let { user ->
                workerRepository.startUploadingWeekly(user.uid, daily_work_request_tag)
                val tempFlow = workerRepository.getWorkInfoByTag(daily_work_request_tag)
                tempFlow.collectLatest {
                    _uploadExpenseWorkerInfo.emit(it)
                }
            }
        }
    }

    fun startWeeklyUploadRequest() {
        viewModelScope.launch {
            dataStore.getUser()?.let { user ->
                workerRepository.startUploadingWeekly(user.uid, weekly_work_request_tag)
                val tempFlow = workerRepository.getWorkInfoByTag(weekly_work_request_tag)
                tempFlow.collectLatest {
                    _uploadExpenseWorkerInfo.emit(it)
                }
            }
        }
    }

    fun startMonthlyUploadRequest() {
        viewModelScope.launch {
            dataStore.getUser()?.let { user ->
                workerRepository.startUploadingMonthly(user.uid, monthly_work_request_tag)
                val tempFlow = workerRepository.getWorkInfoByTag(monthly_work_request_tag)
                tempFlow.collectLatest {
                    _uploadExpenseWorkerInfo.emit(it)
                }
            }
        }
    }
}