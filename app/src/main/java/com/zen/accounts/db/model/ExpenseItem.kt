package com.zen.accounts.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("expense_items")
data class ExpenseItem(
    @PrimaryKey(autoGenerate = true) var id : Long = 0L,
    var itemName : String = "",
    var itemType : String = "",
    var itemPrice : Long = 0L,
    var itemQty : Int = 0,
    var itemAmountType : String = ""
)
