package com.itami.weather_app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ForecastDto(
    @SerializedName("forecastday")
    val forecastDayDtos: List<DayDto>
)
