package com.zen.accounts.ui.screens.main.addexpense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zen.accounts.db.model.Expense
import com.zen.accounts.db.model.ExpenseItem
import com.zen.accounts.db.model.ExpenseType
import com.zen.accounts.ui.screens.main.addexpense.addexpenseitem.ExpenseItemRepository
import com.zen.accounts.ui.screens.main.addexpense.addexpenseitem.ExpenseTypeRepository
import com.zen.accounts.ui.screens.main.addexpense.addexpenseitem.AddExpenseItemUiState
import com.zen.accounts.utility.enums.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val expenseItemRepository : ExpenseItemRepository,
    expenseTypeRepository: ExpenseTypeRepository
): ViewModel() {
    val addExpenseItemUiState by lazy { AddExpenseItemUiState() }
    val addExpenseUiState by lazy { AddExpenseUiState() }
    val allExpenseType : Flow<List<ExpenseType>> = expenseTypeRepository.expenseTypes
    val allExpense : Flow<List<Expense>> = expenseRepository.allExpense
    val allExpenseItem : Flow<List<ExpenseItem>> = expenseItemRepository.allExpenseItem

    fun addExpenseIntoLocalDatabase(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.insertExpenseIntoRoom(expense)
        }
    }

    fun addExpenseItemIntoLocalDatabase(expenseItem : ExpenseItem) {
        viewModelScope.launch(Dispatchers.IO) {
            addExpenseItemUiState.loadingState.value = LoadingState.LOADING
            expenseItemRepository.insertExpenseItemIntoRoom(expenseItem)
            delay(300)
            addExpenseItemUiState.loadingState.value = LoadingState.SUCCESS
        }
    }

    fun deleteExpenseItemsFromLocalDatabase() {
        viewModelScope.launch {
            expenseItemRepository.removeAllExpenseItems()
        }
    }

    fun uploadExpense(expense: Expense) {
        viewModelScope.launch {
            expenseRepository.uploadExpense(expense)
        }
    }
}