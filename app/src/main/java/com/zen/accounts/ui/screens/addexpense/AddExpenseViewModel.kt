package com.zen.accounts.ui.screens.addexpense

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zen.accounts.db.model.Expense
import com.zen.accounts.db.model.ExpenseItem
import com.zen.accounts.db.model.ExpenseType
import com.zen.accounts.repository.ExpenseItemRepository
import com.zen.accounts.repository.ExpenseRepository
import com.zen.accounts.repository.ExpenseTypeRepository
import com.zen.accounts.ui.screens.addexpense.addexpenseitem.AddExpenseItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
        Log.d("asdf", "addExpenseIntoLocalDatabase: $expense")
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.insertExpenseIntoRoom(expense)
        }
    }

    fun addExpenseItemIntoLocalDatabase(expenseItem : ExpenseItem) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseItemRepository.insertExpenseItemIntoRoom(expenseItem)
        }
    }
}