package com.itami.weather_app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherCurrentDto(
    @SerializedName("current")
    val weather: WeatherDto,
)
