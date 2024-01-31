package com.itami.weather_app.data.repository

import android.app.Application
import com.itami.weather_app.R
import com.itami.weather_app.data.mapper.toForecast
import com.itami.weather_app.data.mapper.toWeather
import com.itami.weather_app.data.remote.WeatherApiService
import com.itami.weather_app.domain.model.Forecast
import com.itami.weather_app.domain.model.Weather
import com.itami.weather_app.domain.repository.WeatherRepository
import com.itami.weather_app.domain.util.AppResponse
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApiService: WeatherApiService,
    private val application: Application,
) : WeatherRepository {

    override suspend fun getWeather(cityId: Int): AppResponse<Weather> {
        try {
            val response = weatherApiService.loadCurrentWeather(query = "id:$cityId")
            if (response.isSuccessful) {
                val weather = response.body()?.toWeather() ?: return AppResponse.failed(
                    application.getString(R.string.error_failed_to_load_current_weather)
                )
                return AppResponse.success(weather)
            }

            return AppResponse.failed(application.getString(R.string.error_failed_to_load_current_weather))
        } catch (e: Exception) {
            return AppResponse.failed(application.getString(R.string.error_failed_to_load_current_weather))
        }
    }

    override suspend fun getForecast(cityId: Int): AppResponse<Forecast> {
        try {
            val response = weatherApiService.loadForecast(query = "id:$cityId")
            if (response.isSuccessful) {
                val weather = response.body()?.toForecast() ?: return AppResponse.failed(
                    application.getString(R.string.error_failed_to_load_forecast)
                )
                return AppResponse.success(weather)
            }

            return AppResponse.failed(application.getString(R.string.error_failed_to_load_forecast))
        } catch (e: Exception) {
            return AppResponse.failed(application.getString(R.string.error_failed_to_load_forecast))
        }
    }

}