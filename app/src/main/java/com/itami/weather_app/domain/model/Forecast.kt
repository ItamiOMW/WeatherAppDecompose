package com.itami.weather_app.domain.model

data class Forecast(
    val currentWeather: Weather,
    val upcomingWeather: List<Weather>
)
