package com.itami.weather_app.domain.repository

import com.itami.weather_app.domain.model.Forecast
import com.itami.weather_app.domain.model.Weather
import com.itami.weather_app.domain.util.AppResponse

interface WeatherRepository {

    suspend fun getWeather(cityId: Int): AppResponse<Weather>

    suspend fun getForecast(cityId: Int): AppResponse<Forecast>

}