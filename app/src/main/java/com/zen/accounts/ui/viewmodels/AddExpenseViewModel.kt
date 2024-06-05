package com.zen.accounts.ui.viewmodels

import androidx.lifecycle.viewModelScope
import com.zen.accounts.db.model.Expense
import com.zen.accounts.db.model.ExpenseItem
import com.zen.accounts.repository.ExpenseItemRepository
import com.zen.accounts.repository.ExpenseRepository
import com.zen.accounts.ui.screens.common.LoadingState
import com.zen.accounts.ui.screens.main.add_expense_item.AddExpenseItemUiState
import com.zen.accounts.ui.screens.main.addexpense.AddExpenseUiState
import com.zen.accounts.workmanager.worker_repository.WorkerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
    val allExpenseItem: Flow<ArrayList<ExpenseItem>> =
        expenseItemRepository.allExpenseItem.map {
            val arrayList = arrayListOf<ExpenseItem>()
            arrayList.addAll(it)
            arrayList
        }

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

}