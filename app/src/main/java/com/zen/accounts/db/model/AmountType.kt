package com.zen.accounts.db.model

import com.zen.accounts.utility.enums.DropDownList

data class AmountType(
    override var id: Long = 0L,
    override var value: String = ""
) : DropDownList()
