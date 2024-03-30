package com.zen.accounts.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("expense_type")
data class ExpenseType(
    @PrimaryKey(autoGenerate = true) var id : Long = 0L,
    var type : String = ""
)
