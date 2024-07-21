package com.zen.accounts.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.zen.accounts.db.dao.ExpenseWithOperation
import com.zen.accounts.db.model.Expense
import com.zen.accounts.repository.ExpenseItemRepository
import com.zen.accounts.repository.ExpenseRepository
import com.zen.accounts.ui.screens.common.LoadingState
import com.zen.accounts.ui.screens.main.myexpense.MyExpenseUiState
import com.zen.accounts.ui.screens.main.myexpense.MyExpenseUiStateHolder
import com.zen.accounts.ui.screens.main.myexpense.MyExpenseUiStateHolderCheckBoxList
import com.zen.accounts.ui.screens.main.myexpense.MyExpenseUiStateHolderLoadingState
import com.zen.accounts.ui.screens.main.myexpense.MyExpenseUiStateHolderSelectAll
import com.zen.accounts.ui.screens.main.myexpense.MyExpenseUiStateHolderShowDeleteDialog
import com.zen.accounts.ui.screens.main.myexpense.MyExpenseUiStateHolderShowExpenseList
import com.zen.accounts.ui.screens.main.myexpense.MyExpenseUiStateHolderShowSelectCheckBox
//import com.zen.accounts.ui.screens.main.myexpense.MyExpenseUiStateHolder
import com.zen.accounts.utility.io
import com.zen.accounts.utility.main
import com.zen.accounts.utility.toExpense
import com.zen.accounts.utility.toHashMap
import com.zen.accounts.utility.toLoadingState
import com.zen.accounts.utility.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.memberProperties

@HiltViewModel
class MyExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val expenseItemRepository: ExpenseItemRepository
) : BaseViewmodel() {
    var isMonthlyCalled = false
    var allExpense: List<ExpenseWithOperation> = listOf()
    var monthlyExpense: List<ExpenseWithOperation> = listOf()
    init {
        io {
            expenseRepository.allExpense.collectLatest {
                allExpense = it
            }
            expenseRepository.monthlyExpense.collectLatest {
                allExpense = it
            }
        }
    }

    private val _myExpenseUiStateFlow = MutableStateFlow(MyExpenseUiStateHolder())
    val myExpenseUiStateFlow = _myExpenseUiStateFlow.asStateFlow()

    private fun updateMyExpenseUiStateFlow(newState: MyExpenseUiStateHolder) {
        _myExpenseUiStateFlow.update { newState }
    }

    fun deleteExpenses(expenses: List<Long>) {
        io {
            updateMyExpenseUiState(LoadingState.LOADING, MyExpenseUiStateHolderLoadingState)
            updateMyExpenseUiState(hashMapOf<Long, String>(), MyExpenseUiStateHolderCheckBoxList)
            updateMyExpenseUiState(false, MyExpenseUiStateHolderShowSelectCheckBox)
            updateMyExpenseUiState(false, MyExpenseUiStateHolderSelectAll)
            expenseRepository.deleteExpenses(expenses)
            delay(500)
            updateMyExpenseUiState(LoadingState.SUCCESS, MyExpenseUiStateHolderLoadingState)
        }
    }

    fun updateMyExpenseUiState(newValue: Any, fieldName: String) {
        io {
            val temp = myExpenseUiStateFlow.value.copy()
            when (fieldName) {
                MyExpenseUiStateHolderShowSelectCheckBox -> {
                    temp.showSelectCheckbox = newValue.toString().toBoolean()
                }

                MyExpenseUiStateHolderCheckBoxList -> {
                    temp.checkBoxMap = newValue.toHashMap()
                    if (!isMonthlyCalled)
                        temp.selectAll = newValue.toHashMap().size == allExpense.size
                    else
                        temp.selectAll = newValue.toHashMap().size == monthlyExpense.size
                }

                MyExpenseUiStateHolderSelectAll -> {
                    temp.selectAll = newValue.toString().toBoolean()
                }

                MyExpenseUiStateHolderLoadingState -> {
                    temp.loadingState = newValue.toLoadingState()
                }

                MyExpenseUiStateHolderShowDeleteDialog -> {
                    temp.showDeleteDialog = newValue.toString().toBoolean()
                }

                MyExpenseUiStateHolderShowExpenseList -> {
                    temp.showExpenseList = newValue.toString().toBoolean()
                }
            }
            updateMyExpenseUiStateFlow(temp)
        }
    }

}