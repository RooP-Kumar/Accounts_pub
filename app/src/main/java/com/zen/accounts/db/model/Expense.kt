package com.zen.accounts.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.zen.accounts.utility.DateLongConverter
import com.zen.accounts.utility.ListOfExpenseItemToStringConverter
import java.util.Date

@Entity("expenses")
@TypeConverters(ListOfExpenseItemToStringConverter::class, DateLongConverter::class)
data class Expense(
    @PrimaryKey val id : Long = 0L,
    val title : String = "",
    val items : List<ExpenseItem> = listOf(),
    val totalAmount : Long = 0L,
    var date : String = ""
)
