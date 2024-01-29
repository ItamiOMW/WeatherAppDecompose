package com.itami.weather_app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DayWeatherDto(
    @SerializedName("avgtemp_c")
    val tempC: Float,
    @SerializedName("condition")
    val condition: ConditionDto
)