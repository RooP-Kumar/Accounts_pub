package com.zen.accounts.db.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.zen.accounts.utility.DateStringConverter
import com.zen.accounts.utility.ListOfExpenseItemToStringConverter
import java.util.Date

@Entity("expenses")
@TypeConverters(ListOfExpenseItemToStringConverter::class, DateStringConverter::class)
data class Expense(
    @PrimaryKey
    val id : Long = 0L,
    val title : String = "",
    val items : List<ExpenseItem> = listOf(),
    val totalAmount : Double = 0.0,
    var date : Date = Date()
)
