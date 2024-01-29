package com.itami.weather_app.domain.usecase

import com.itami.weather_app.domain.model.Weather
import com.itami.weather_app.domain.repository.WeatherRepository

class GetWeatherUseCase(
    private val weatherRepository: WeatherRepository
) {

    suspend operator fun invoke(cityId: Int): Weather {
        return weatherRepository.getWeather(cityId = cityId)
    }

}