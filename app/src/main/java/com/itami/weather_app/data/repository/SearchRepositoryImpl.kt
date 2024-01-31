package com.itami.weather_app.data.repository

import android.app.Application
import com.itami.weather_app.R
import com.itami.weather_app.data.mapper.toCityList
import com.itami.weather_app.data.remote.WeatherApiService
import com.itami.weather_app.domain.model.City
import com.itami.weather_app.domain.repository.SearchRepository
import com.itami.weather_app.domain.util.AppResponse
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val weatherApiService: WeatherApiService,
    private val application: Application
): SearchRepository {

    override suspend fun searchCity(query: String): AppResponse<List<City>> {
        try {
            val response = weatherApiService.searchCity(query = query)
            if (response.isSuccessful) {
                val cities = response.body()?.toCityList() ?: emptyList()
                return AppResponse.success(cities)
            }

            return AppResponse.failed(application.getString(R.string.error_failed_to_load_cities))
        } catch (e: Exception) {
            return AppResponse.failed(application.getString(R.string.error_failed_to_load_cities))
        }
    }

}