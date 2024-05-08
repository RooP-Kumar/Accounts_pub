package com.zen.accounts.utility.enums

import android.os.Bundle
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.ui.unit.Dp

enum class LoadingState {
    IDLE,
    LOADING,
    SUCCESS,
    FAILURE
}



object dpSaver : Saver<MutableState<Dp>, Bundle> {
    override fun SaverScope.save(value: MutableState<Dp>): Bundle? {
        return Bundle().apply {
            putFloat("dpValue", value.value.value)
        }
    }

    override fun restore(value: Bundle): MutableState<Dp> {
        return mutableStateOf(Dp(value.getFloat("dpValue", 0f)))
    }

}

open class DropDownList{
    open var id : Long = 0L
    open var value: String = ""
}