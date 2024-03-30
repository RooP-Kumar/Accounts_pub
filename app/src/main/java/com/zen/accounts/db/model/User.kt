package com.zen.accounts.db.model

data class User(
    var name: String = "",
    var phone : String = "",
    var email : String = "",
    var isAuthenticated : Boolean = false
)
