package com.itami.weather_app.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Long.secondsToCalendar(): Calendar = Calendar.getInstance().apply {
    time = Date(this@secondsToCalendar * 1000)
}

fun Long.millisToCalendar(): Calendar = Calendar.getInstance().apply {
    time = Date(this@millisToCalendar)
}

fun Calendar.formattedFullDate(
    locale: Locale = Locale.getDefault()
): String {
    val formatter = SimpleDateFormat("EEEE | d MMM y", locale)
    return formatter.format(this.time)
}

fun Calendar.formattedShortDayOfWeek(
    locale: Locale = Locale.getDefault()
): String {
    val formatter = SimpleDateFormat("EEE", locale)
    return formatter.format(this.time)
}