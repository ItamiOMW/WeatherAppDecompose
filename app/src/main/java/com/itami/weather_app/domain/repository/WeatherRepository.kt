package com.itami.weather_app.domain.repository

import com.itami.weather_app.domain.model.Forecast
import com.itami.weather_app.domain.model.Weather

interface WeatherRepository {

    suspend fun getWeather(cityId: Int): Weather

    suspend fun getForecast(cityId: Int): Forecast

}