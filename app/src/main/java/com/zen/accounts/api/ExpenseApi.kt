
package com.zen.accounts.api

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.zen.accounts.api.resource.Response
import com.zen.accounts.api.retrofit.ExpenseService
import com.zen.accounts.db.model.Expense
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ExpenseApi @Inject constructor(
    private val expenseService: ExpenseService
) {
    suspend fun uploadToFirebase(uid: String, expense: List<Expense>) : Response<Unit> = suspendCoroutine { continuation ->
        val response = Response(value = Unit)
        val db = FirebaseFirestore.getInstance()
        val colRef = db.collection("Users").document(uid).collection("expenses")
        db.runTransaction { trans ->
            expense.forEach {expense ->
                trans.set(colRef.document(expense.id.toString()), expense)
            }
        }
            .addOnSuccessListener {
                response.status = true
                response.message = it.toString()
                continuation.resume(response)
            }
            .addOnFailureListener {
                response.status = false
                response.message = it.message.toString()
                continuation.resume(response)
            }
    }

    suspend fun getExpenseFromFirebase(uid: String) : Response<List<Expense>> = suspendCoroutine { continuation ->
        val response = Response(value = listOf<Expense>())
        val db = FirebaseFirestore.getInstance()
        Log.d("asdf", "getExpenseFromFirebase: $uid")
        db.collection("Users").document(uid).collection("expenses")
        .get()
            .addOnSuccessListener {
                response.value = it.toObjects(Expense::class.java)
                response.status = true
                response.message = "Success!"
                continuation.resume(response)
            }
            .addOnFailureListener {
                response.status = false
                response.message = it.message.toString()
                continuation.resume(response)
            }
    }

    suspend fun deleteFromFirebase(uid: String, expenseIds: List<Long>) : Response<Unit> = suspendCoroutine { continuation ->
        val response = Response(value = Unit)
        val db = FirebaseFirestore.getInstance()
        val colRef = db.collection("Users").document(uid).collection("expenses")
        db.runTransaction { trans ->
            expenseIds.forEach {id ->
                trans.delete(colRef.document(id.toString()))
            }
        }
            .addOnSuccessListener {
                response.status = true
                response.message = it.toString()
                continuation.resume(response)
            }
            .addOnFailureListener {
                response.status = false
                response.message = it.message.toString()
                continuation.resume(response)
            }
    }

}