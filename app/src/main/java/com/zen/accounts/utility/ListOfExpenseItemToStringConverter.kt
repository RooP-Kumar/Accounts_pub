package com.zen.accounts.utility

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zen.accounts.db.model.ExpenseItem


class ListOfExpenseItemToStringConverter() {
    @TypeConverter
    fun listOfExpenseItemToString(data : ArrayList<ExpenseItem>) : String {
        val gson = Gson()
        return gson.toJson(data)
    }

    @TypeConverter
    fun stringToListOfExpenseItem(listString : String) : ArrayList<ExpenseItem> {
        val gson = Gson()
        return gson.fromJson(listString, object : TypeToken<ArrayList<ExpenseItem>>(){}.type)
    }
}