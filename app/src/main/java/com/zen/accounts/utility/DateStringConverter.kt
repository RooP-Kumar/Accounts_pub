package com.zen.accounts.utility

import android.util.Log
import androidx.room.TypeConverter
import com.zen.accounts.ui.theme.date_formatter_patter_string
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

class DateStringConverter {
    @TypeConverter
    fun dateToString(date: Date) : String {
        val formatter = SimpleDateFormat(date_formatter_patter_string, java.util.Locale.UK)
        return formatter.format(date)
    }

    @TypeConverter
    fun stringToDate(dateString : String) : Date {
        val formatter = SimpleDateFormat(date_formatter_patter_string, java.util.Locale.UK)
        return formatter.parse(dateString)!!
    }
}

//LLL dd, yyyy hh:mm:ss a