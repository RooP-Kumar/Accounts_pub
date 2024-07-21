package com.zen.accounts.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.zen.accounts.db.dao.ExpenseWithOperation
import com.zen.accounts.db.model.Expense
import com.zen.accounts.repository.ExpenseItemRepository
import com.zen.accounts.repository.ExpenseRepository
import com.zen.accounts.ui.screens.common.LoadingState
import com.zen.accounts.ui.screens.main.myexpense.MyExpenseUiState
//import com.zen.accounts.ui.screens.main.myexpense.MyExpenseUiStateHolder
import com.zen.accounts.utility.io
import com.zen.accounts.utility.toExpense
import com.zen.accounts.utility.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    val allExpense : Flow<List<ExpenseWithOperation>> = expenseRepository.allExpense
    val monthlyExpense : Flow<List<ExpenseWithOperation>> = expenseRepository.monthlyExpense

//    private val _myExpenseUiStateFlow = MutableStateFlow(MyExpenseUiStateHolder())
//    val myExpenseUiStateFlow = _myExpenseUiStateFlow.asStateFlow()
//
//    fun updateMyExpenseUiStateFlow(newState: MyExpenseUiStateHolder) {
//        _myExpenseUiStateFlow.update { newState }
//    }

    val myExpenseUiState by lazy { MyExpenseUiState() }

    fun  deleteExpenses(expenses : List<Long>) {
        io {
            myExpenseUiState.apply {
                loadingState.value = LoadingState.LOADING
                checkBoxMap.clear()
                showSelectCheckbox.value = false
                selectAll.value = false
                expenseRepository.deleteExpenses(expenses)
                delay(500)
                loadingState.value = LoadingState.SUCCESS
            }
        }
    }

//    fun updateMyExpenseUiState(newValue : Any, fieldName : String) {
//        val temp = myExpenseUiStateFlow.value
//        val param = this::class.memberProperties
//            .filterIsInstance<KMutableProperty0<Any>>()
//            .associateBy { it.name }
//        try {
//            param[fieldName]?.set(newValue)
//            _myExpenseUiStateFlow.update { temp }
//        } catch (e: Exception) {
//            context.toast(e.message.toString())
//        }
//    }

}