package com.zen.accounts.repository

import com.zen.accounts.db.dao.ExpenseDao
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val addExpenseDao: ExpenseDao
) {



}