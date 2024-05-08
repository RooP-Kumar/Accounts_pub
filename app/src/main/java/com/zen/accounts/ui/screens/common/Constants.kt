package com.zen.accounts.ui.theme

// <------------------ Navigation Routes ------------------>
const val splash_route = "Splash Route"
const val auth_route = "Authentication Route"
const val main_route = "Application Route"

// <------------------ Screen Routes ------------------>
const val splash_screen_route = "Splash Screen Route"
const val login_screen_route = "Login Screen Route"
const val register_screen_route = "Register Screen Route"
const val add_expense_screen_route = "Add Expense Screen Route"
const val my_expense_screen_route = "My Expense Screen Route"
const val setting_screen_route = "Setting Screen Route"
const val logout_route = "Logout Route"
const val add_expense_item_screen_route = "Add Expense item Route"

// <------------------ Screen Label Strings ------------------>
const val add_expense_screen_label = "Add Expense"
const val my_expense_screen_label = "My Expense"
const val setting_screen_label = "Setting"
const val login_label = "Login"
const val register_label = "Register"

// <------------------ Placeholder Text Strings ------------------>
const val enter_name = "Name"
const val enter_email = "Email"
const val enter_phone = "Phone"
const val enter_pass = "Password"

// <------------------ Some Constant Strings ------------------>
const val did_not_have_account = "Didn't have an account?"
const val already_have_account = "already have an account?"

// <------------------ Data Store Strings ------------------>
const val datastore_name = "user data store"
const val user_data_store_key = "user data store key"
const val system_in_dark_mode = "system in dark mode"

// <------------------ Buttons Label Strings ------------------>
const val login_button_label = "LOGIN"
const val register_button_label = "REGISTER"
const val add_item_button_label = "ADD ITEM"

// <------------------ Rupees Sign Amount String ------------------>
fun getRupeeString(amount: Long) : String {
    if(amount == 0L) return ""
    return "\u20B9 $amount"
}

// <------------------ Date Formatter Pattern String ------------------>
const val date_formatter_patter_string = "dd LLL, yyyy hh:mm a"