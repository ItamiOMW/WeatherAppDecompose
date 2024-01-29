package com.itami.weather_app.domain.usecase

import com.itami.weather_app.domain.model.Forecast
import com.itami.weather_app.domain.repository.WeatherRepository

class GetForecastUseCase(
    private val weatherRepository: WeatherRepository
) {

    suspend operator fun invoke(cityId: Int): Forecast {
        return weatherRepository.getForecast(cityId = cityId)
    }

}