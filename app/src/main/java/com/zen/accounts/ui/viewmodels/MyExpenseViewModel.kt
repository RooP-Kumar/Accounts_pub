package com.zen.accounts.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.zen.accounts.db.dao.ExpenseWithOperation
import com.zen.accounts.db.model.Expense
import com.zen.accounts.repository.ExpenseItemRepository
import com.zen.accounts.repository.ExpenseRepository
import com.zen.accounts.ui.screens.common.LoadingState
import com.zen.accounts.ui.screens.main.myexpense.MyExpenseUiState
import com.zen.accounts.utility.io
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MyExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val expenseItemRepository: ExpenseItemRepository
) : ViewModel() {
    val allExpense : Flow<List<ExpenseWithOperation>> = expenseRepository.allExpense
    val myExpenseUiState by lazy { MyExpenseUiState() }
    var variable = 0
    fun deleteExpenses(expenses : List<Expense>) {
        io {
            myExpenseUiState.apply {
                loadingState.value = LoadingState.LOADING
                val tempList = arrayListOf<Expense>()
                val tempTwoList = arrayListOf<Boolean>()
                checkBoxList.forEachIndexed { ind, item ->
                    if(item){
                        tempList.add(expenses[ind])
                    } else {
                        tempTwoList.add(false)
                    }
                }
                checkBoxList.clear()
                checkBoxList.addAll(tempTwoList)
                showSelectCheckbox.value = false
                selectAll.value = false
                totalSelectedItem.value = 0
                expenseRepository.deleteExpenses(tempList)
                delay(500)
                loadingState.value = LoadingState.SUCCESS
            }

        }
    }

}