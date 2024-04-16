package com.zen.accounts.ui.screens.main.addexpense.addexpenseitem

import com.zen.accounts.db.dao.ExpenseTypeDao
import com.zen.accounts.db.model.ExpenseType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExpenseTypeRepository @Inject constructor(
    private val expenseTypeDao : ExpenseTypeDao
) {

    val expenseTypes : Flow<List<ExpenseType>> = expenseTypeDao.getAllExpenseType()

}