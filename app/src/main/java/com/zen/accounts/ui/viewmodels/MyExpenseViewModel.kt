package com.zen.accounts.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.zen.accounts.db.model.Expense
import com.zen.accounts.repository.ExpenseRepository
import com.zen.accounts.ui.screens.main.myexpense.MyExpenseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MyExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {
    val allExpense : Flow<List<Expense>> = expenseRepository.allExpense
    val myExpenseUiState by lazy { MyExpenseUiState() }

}