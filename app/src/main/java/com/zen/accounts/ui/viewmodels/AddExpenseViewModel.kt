package com.zen.accounts.ui.screens.main.addexpense

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.zen.accounts.db.model.Expense
import com.zen.accounts.db.model.ExpenseItem
import com.zen.accounts.repository.ExpenseRepository
import com.zen.accounts.ui.screens.common.LoadingState
import com.zen.accounts.ui.screens.main.addexpenseitem.AddExpenseItemUiState
import com.zen.accounts.repository.ExpenseItemRepository
import com.zen.accounts.repository.WorkerRepository
import com.zen.accounts.workmanager.UploadExpenseWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val expenseItemRepository: ExpenseItemRepository,
    private val workerRepository: WorkerRepository
) : ViewModel() {
    val addExpenseItemUiState by lazy { AddExpenseItemUiState() }
    val addExpenseUiState by lazy { AddExpenseUiState() }
    val allExpenseItem: Flow<List<ExpenseItem>> = expenseItemRepository.allExpenseItem
    private val _singleUploadExpenseWorkerInfo : MutableStateFlow<List<WorkInfo>?> = MutableStateFlow(listOf())
    val singleUploadExpenseWorkerInfo : Flow<List<WorkInfo>?> get() = _singleUploadExpenseWorkerInfo

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
            expenseItemRepository.removeAllExpenseItems()
        }
    }

    fun startSingleUploadRequest() {
        viewModelScope.launch {
            workerRepository.startUploadingNow("9528865314Roop@roopkm12", "singleUniqueWorkTag")
            val tempFlow = workerRepository.getWorkInfoByTag("singleUniqueWorkTag")
            tempFlow.collectLatest {
                _singleUploadExpenseWorkerInfo.emit(it)
            }
        }
    }

}