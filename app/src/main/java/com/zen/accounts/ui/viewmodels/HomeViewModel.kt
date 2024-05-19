package com.zen.accounts.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.zen.accounts.repository.ExpenseRepository
import com.zen.accounts.ui.screens.main.home.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {
    val allExpense get() = expenseRepository.allExpense

    val homeUiState by lazy {
        HomeUiState()
    }

}