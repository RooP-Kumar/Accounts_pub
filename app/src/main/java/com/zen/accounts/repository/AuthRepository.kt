package com.zen.accounts.repository

import android.util.Log
import com.zen.accounts.api.AuthApi
import com.zen.accounts.api.resource.Resource
import com.zen.accounts.api.resource.Response
import com.zen.accounts.db.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApi: AuthApi
) {
    suspend fun registerUser(user: User, pass: String) : Resource<Response<String>> {
        return withContext(Dispatchers.IO) {
            val res = authApi.registerUsingEmailAndPassword(generateUID(user), pass, user)
            if(res.status) Resource.SUCCESS(res)
            else Resource.FAILURE(res.message)
        }
    }

    suspend fun loginUser(email: String, pass: String) : Resource<Response<User>> {
        return withContext(Dispatchers.IO) {
            val res = authApi.loginUsingEmailAndPassword(email, pass)
            if(res.status) Resource.SUCCESS(res)
            else Resource.FAILURE(res.message)
        }
    }

    suspend fun logout() {
        withContext(Dispatchers.IO) {
            authApi.logout()
        }
    }

    private fun generateUID(user : User) : String {
        val first = user.phone.substring(0, 5)
        var middleOne = ""
        for (i in 0..<user.name.length){
            if(user.name[i] == ' '){
                break;
            }
            middleOne += user.name[i]
        }
        var middleTwo = ""
        for (i in 0..<user.email.length) {
            if (user.email[i] == '@'){
                break;
            }
            middleTwo += user.email[i]
        }
        val end = user.phone.substring(5, 10)
        return "$first$middleOne@$middleTwo$end"
    }
}