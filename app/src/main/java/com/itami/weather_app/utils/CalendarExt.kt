package com.itami.weather_app.utils

import java.util.Calendar
import java.util.Date

fun Long.secondsToCalendar(): Calendar = Calendar.getInstance().apply {
    time = Date(this@secondsToCalendar * 1000)
}

fun Long.millisToCalendar(): Calendar = Calendar.getInstance().apply {
    time = Date(this@millisToCalendar)
}