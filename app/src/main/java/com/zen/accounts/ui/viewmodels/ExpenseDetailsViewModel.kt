package com.zen.accounts.ui.viewmodels

import com.zen.accounts.db.model.Expense
import com.zen.accounts.db.model.ExpenseItem
import com.zen.accounts.repository.ExpenseRepository
import com.zen.accounts.ui.screens.main.expenseDetail.ExpenseDetailUIState
import com.zen.accounts.utility.io
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExpenseDetailsViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
) : BaseViewmodel() {

    val expenseDetailsUiState by lazy { ExpenseDetailUIState() }

    fun updateExpense(updatedExpenseItem : ExpenseItem?, itemInd: Int) {
        io {
            expenseDetailsUiState.apply {
                if(updatedExpenseItem != null) {
                    expense.value.totalAmount -= expenseItems[itemInd].itemAmount!!.toDouble()
                    expense.value.totalAmount += updatedExpenseItem.itemAmount!!.toDouble()
                    expenseItems[itemInd] = updatedExpenseItem
                    expense.value.items[itemInd] = updatedExpenseItem
                }
                expenseRepository.updateExpense(expense.value)
                val temp = expense.value.copy()
                expense.value = Expense()
                expense.value = temp
            }
        }
    }

    fun deleteExpense() {
        io {
            expenseDetailsUiState.apply {
                expenseRepository.deleteExpenses(listOf(expense.value))
            }
        }
    }

    fun deleteExpenseItem(itemInd: Int) {
        io {
            expenseDetailsUiState.apply {
                expense.value.totalAmount -= expenseItems[itemInd].itemAmount!!.toDouble()
                expenseItems.removeAt(itemInd)
                expense.value.items.removeAt(itemInd)
                expenseRepository.updateExpense(expense.value)
                val temp = expense.value.copy()
                expense.value = Expense()
                expense.value = temp
                showDeleteDialog.value = false
            }
        }
    }
}