package com.itami.weather_app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ConditionDto(
    @SerializedName("text")
    val text: String,
    @SerializedName("icon")
    val iconUrl: String,
    @SerializedName("code")
    val code: Int,
)
