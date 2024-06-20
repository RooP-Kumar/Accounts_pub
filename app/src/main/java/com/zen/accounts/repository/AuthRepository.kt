package com.zen.accounts.repository

import com.zen.accounts.api.AuthApi
import com.zen.accounts.api.ProfileApi
import com.zen.accounts.api.resource.Resource
import com.zen.accounts.api.resource.Response
import com.zen.accounts.db.model.User
import com.zen.accounts.utility.io
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val profileApi: ProfileApi
) : BaseRepository() {
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
            if(res.status) {
                dataStore.saveProfilePic(res.value.second)
                Resource.SUCCESS(
                    Response(
                        value = res.value.first,
                        status = res.status,
                        res.message
                    )
                )
            }
            else Resource.FAILURE(res.message)
        }
    }

    suspend fun logout() {
        io {
            authApi.logout()
        }
    }

    suspend fun uploadProfilePic(): Resource<Response<Unit>> {
        val user = dataStore.getUser()
        val profilePic = dataStore.getProfilePic()
        return withContext(Dispatchers.IO) {
            if(user != null && profilePic != null) {
                val res = profileApi.updateProfilePic(user, profilePic)
                if (res.status) Resource.SUCCESS(value = res)
                else Resource.FAILURE(res.message)
            } else {
                Resource.FAILURE("Invalid User")
            }
        }
    }

    fun generateUID(user : User) : String {
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