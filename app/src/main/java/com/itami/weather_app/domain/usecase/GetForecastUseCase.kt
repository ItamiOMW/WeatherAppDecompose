package com.itami.weather_app.domain.usecase

import com.itami.weather_app.domain.model.Forecast
import com.itami.weather_app.domain.repository.WeatherRepository
import com.itami.weather_app.domain.util.AppResponse

class GetForecastUseCase(
    private val weatherRepository: WeatherRepository
) {

    suspend operator fun invoke(cityId: Int): AppResponse<Forecast> {
        return weatherRepository.getForecast(cityId = cityId)
    }

}