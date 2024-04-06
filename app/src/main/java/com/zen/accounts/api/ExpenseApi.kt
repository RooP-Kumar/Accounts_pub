
package com.zen.accounts.api

import com.zen.accounts.api.retrofit.ExpenseService
import com.zen.accounts.db.model.Expense
import com.zen.accounts.api.resource.Response
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ExpenseApi @Inject constructor(
    private val expenseService: ExpenseService
) {

    suspend fun getAllExpenseFromWeb() : Response<List<Expense>> = suspendCoroutine {continuation ->
        val response = Response(value = listOf<Expense>())
        val apiResponse = expenseService.getAllExpense().execute()
        if (apiResponse.isSuccessful) {
            response.value = apiResponse.body()!!
            response.status = true
            response.message = apiResponse.message()
            continuation.resume(response)
        } else {
            response.message = apiResponse.message()
            continuation.resume(response)
        }
    }

}