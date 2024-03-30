package com.zen.accounts.utility

import com.google.gson.Gson
import com.zen.accounts.db.model.User

fun userToString(user : User) : String {
    val gson = Gson()
    return gson.toJson(user)
}

fun stringToUser(userString : String) : User {
    val gson = Gson()
    return gson.fromJson(userString, User::class.java)
}