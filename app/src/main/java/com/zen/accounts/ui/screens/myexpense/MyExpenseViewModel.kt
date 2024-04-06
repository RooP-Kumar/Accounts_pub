package com.zen.accounts.ui.screens.myexpense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zen.accounts.db.model.Expense
import com.zen.accounts.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {
    val allExpense : Flow<List<Expense>> = expenseRepository.allExpense
    val myExpenseUiState by lazy { MyExpenseUiState() }
    val allExpenseFromApi : Flow<List<Expense>> = expenseRepository.allExpenseFromApi

    fun getAllExpenseFromApi() {
        viewModelScope.launch {
            expenseRepository.getAllExpensesFromApi()
        }
    }
}