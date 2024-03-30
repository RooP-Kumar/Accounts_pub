package com.zen.accounts.utility

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.zen.accounts.db.model.ExpenseItem


class ListOfExpenseItemToStringConverter() {
    @TypeConverter
    fun listOfExpenseItemToString(data : List<ExpenseItem>) : String {
        val gson = Gson()
        return gson.toJson(data)
    }

    @TypeConverter
    fun stringToListOfExpenseItem(listString : String) : List<ExpenseItem> {
        val gson = Gson()
        return gson.fromJson(listString, Array<ExpenseItem>::class.java).toList()
    }
}