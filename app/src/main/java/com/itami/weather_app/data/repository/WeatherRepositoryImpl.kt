package com.itami.weather_app.data.repository

import com.itami.weather_app.domain.model.Forecast
import com.itami.weather_app.domain.model.Weather
import com.itami.weather_app.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(

): WeatherRepository {

    override suspend fun getWeather(cityId: Int): Weather {
        TODO("Not yet implemented")
    }

    override suspend fun getForecast(cityId: Int): Forecast {
        TODO("Not yet implemented")
    }

}