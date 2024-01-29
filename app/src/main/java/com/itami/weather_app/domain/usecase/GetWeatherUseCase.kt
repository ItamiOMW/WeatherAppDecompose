package com.itami.weather_app.domain.usecase

import com.itami.weather_app.domain.model.Weather
import com.itami.weather_app.domain.repository.WeatherRepository
import com.itami.weather_app.domain.util.AppResponse

class GetWeatherUseCase(
    private val weatherRepository: WeatherRepository
) {

    suspend operator fun invoke(cityId: Int): AppResponse<Weather> {
        return weatherRepository.getWeather(cityId = cityId)
    }

}