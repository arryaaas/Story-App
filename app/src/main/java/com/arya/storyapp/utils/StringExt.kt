package com.arya.storyapp.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun String.withDateFormat(): String {
    val format = SimpleDateFormat("yyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    val date = format.parse(this) as Date
    return DateFormat.getDateInstance(DateFormat.LONG).format(date)
}