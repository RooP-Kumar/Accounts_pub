
package com.zen.accounts.api

import com.zen.accounts.api.retrofit.ExpenseService
import com.zen.accounts.db.model.Expense
import com.zen.accounts.api.resource.Response
import java.lang.Exception
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

    suspend fun uploadExpense(expense: Expense) : Response<Long> {
        val response = Response(0L)
        try {
            val apiResponse = expenseService.uploadExpense(expense)
            return if(apiResponse.isSuccessful) {
                response.value = apiResponse.body()!!
                response.status = true
                response.message = apiResponse.message()
                response
            } else {
                response.status = false
                response.message = apiResponse.message()
                response
            }
        } catch (e : Exception) {
            response.status = false
            response.message = e.message.toString()
            return response
        }

    }

}