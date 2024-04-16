package com.zen.accounts.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.JsonParser
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.zen.accounts.ui.theme.enter_name
import com.zen.accounts.utility.DateStringConverter
import com.zen.accounts.utility.ListOfExpenseItemToStringConverter
import java.time.*;
import java.util.Date

@Entity("expenses")
@TypeConverters(ListOfExpenseItemToStringConverter::class, DateStringConverter::class)
data class Expense(
    @PrimaryKey val id : Long = 0L,
    val title : String = "",
    val items : List<ExpenseItem> = listOf(),
    val totalAmount : Long = 0L,
    var date : Date = Date()
) {

}
