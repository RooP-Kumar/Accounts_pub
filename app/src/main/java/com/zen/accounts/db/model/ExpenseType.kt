package com.zen.accounts.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zen.accounts.utility.enums.DropDownList

@Entity("expense_type")
data class ExpenseType(
    @PrimaryKey(autoGenerate = true) override var id : Long = 0L,
    override var value : String = ""
): DropDownList()
