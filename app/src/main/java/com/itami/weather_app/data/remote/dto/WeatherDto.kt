package com.itami.weather_app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherDto(
    @SerializedName("last_updated_epoch")
    val date: Long,
    @SerializedName("temp_c")
    val tempC: Float,
    @SerializedName("condition")
    val conditionDto: ConditionDto
)
