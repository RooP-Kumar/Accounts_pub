package com.zen.accounts.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("expense_items")
data class ExpenseItem(
    @PrimaryKey(autoGenerate = true) var id : Long,
    var itemTitle : String,
    var itemAmount : Double?
) {
    constructor() : this(0L, "", 0.0)
}