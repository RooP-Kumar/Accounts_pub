package com.zen.accounts.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.zen.accounts.utility.DateStringConverter
import com.zen.accounts.utility.ListOfExpenseItemToStringConverter
import java.util.Date

@Entity("expenses")
@TypeConverters(ListOfExpenseItemToStringConverter::class, DateStringConverter::class)
data class Expense(
    @PrimaryKey
    var id : Long = 0L,
    var title : String = "",
    val items : ArrayList<ExpenseItem> = arrayListOf(),
    var totalAmount : Double = 0.0,
    var date : Date = Date()
)
