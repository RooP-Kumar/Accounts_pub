package com.zen.accounts.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.zen.accounts.utility.DateStringConverter
import java.util.Date

@Entity("expense_items")
@TypeConverters(DateStringConverter::class)
data class ExpenseItem(
    @PrimaryKey(autoGenerate = true) var id : Long,
    var itemTitle : String,
    var itemAmount : Double?,
    var lastUpdate : Date = Date()
) {
    constructor() : this(0L, "", 0.0)
}