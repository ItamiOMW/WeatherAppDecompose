package com.itami.weather_app.domain.model

import java.util.Calendar

data class Weather(
    val tempC: Float,
    val condition: String,
    val conditionIconUrl: String,
    val date: Calendar
)
