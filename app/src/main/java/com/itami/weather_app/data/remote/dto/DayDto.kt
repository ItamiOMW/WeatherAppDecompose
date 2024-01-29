package com.itami.weather_app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DayDto(
    @SerializedName("date_epoch")
    val datetime: Long,
    @SerializedName("day")
    val dayWeather: DayWeatherDto,
)
