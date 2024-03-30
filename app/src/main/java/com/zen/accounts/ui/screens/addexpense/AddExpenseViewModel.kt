package com.zen.accounts.ui.screens.addexpense

import androidx.lifecycle.ViewModel
import com.zen.accounts.db.model.ExpenseType
import com.zen.accounts.repository.ExpenseRepository
import com.zen.accounts.repository.ExpenseTypeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val expenseTypeRepository: ExpenseTypeRepository
): ViewModel() {
    val addExpenseUiState by lazy { AddExpenseUiState() }
    val allExpenseType : Flow<List<ExpenseType>> = expenseTypeRepository.expenseTypes
}