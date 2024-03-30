package com.zen.accounts.utility

import androidx.room.TypeConverter
import java.util.Date

class DateLongConverter {
    @TypeConverter
    fun dateToLong(date: Date) : Long {
        return date.time
    }

    @TypeConverter
    fun longToDate(dateLong : Long) : Date {
        return Date(dateLong)
    }
}